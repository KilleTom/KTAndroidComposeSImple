# 五天自学 Compose 从 0 到 1 构建一个 WanAndroid 客户端

这边文章主要分享下如何自学 Compose ，五天时间内从 0 到 1 构建一个 WanAndroid 客户端。

先来看看效果图

| 黑夜模式                                                     |                        明亮模式                         |
| ------------------------------------------------------------ | :-----------------------------------------------------: |
| <img src=".\document\d_model.gif"/> | <img src=".\document\l_model.gif"/> |

这篇文章主要是针对初学者如何自学 `Compose` 及 `MVI` ：

## Day 1

学习 `Compose` 最重要的是要掌握重组概念，也就说 `Compose` 没有原来 `Android` 测绘机制，`Compose` 中控件的显示及状态描述，都是通过 `state` 状态机一样的机制来决定，譬如闪屏页跳转到首页显示。

其次需要掌握：`modifi`, 黑夜模式与明亮模式下的切换适配

```kotlin
@Composable
fun KTAndroidComposeSimpleTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    //利用监听黑夜模式切换状态动态设置 App 主题
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val systemUiCtrl = rememberSystemUiController()

    SideEffect {

        systemUiCtrl.setSystemBarsColor(
            color = Color.Transparent,
            isNavigationBarContrastEnforced = false
        )
    }
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = { ProvideWindowInsets(content = content) }
    )


}
```

熟悉 `Modifier` 掌握控件形状的描述譬如实现闪屏页面：

|                        黑夜模式                         |                        明亮模式                         |
| :-----------------------------------------------------: | :-----------------------------------------------------: |
| <img src=".\document\d_splash.png"/> | <img src=".\document\l_splash.png"/> |

## Day 2

关注 `Compose` 下 `navigation` 的使用掌握所有页面的跳转控制，注意一点的 `NavHostController` 作为控制整个导航动作的时候，声明使用的过程中最好不要一层层嵌套下去注册路由避免出现利用 `hint` 初始化 `model` 的时候出现奔溃。

掌握 `Scaffold` 脚本架这个超级布局的使用，如何利用他实现侧滑、添加悬浮按钮、底部导航栏等。譬如实现一下页面：

<img src=".\document\home_l.png"/>

## Day 3

学习 `Compose` 下的`panding` 级 `Lay` 相关布局，是如何能够做到类似 `RecycleVIew` 列表效果，以及如何封装一个通用的分页请求工具

```kotlin
fun <T : Any> ViewModel.easyPager(
    config: BasePagingConfig = BasePagingConfig(),
    callAction: suspend (page: Int) -> BasicBean<ListWrapper<T>>
): Flow<PagingData<T>> {
    return basePager(config, 0) {
        val page = it.key ?: 0
        val response = try {
            HttpResult.Success(callAction.invoke(page))
        } catch (e: Exception) {
            if (NetTools.checkNetUsefull(LearnAndroidApp.CONTEXT).not()) {
                showToast("没有网络,请重试")
            } else {
                showToast("请求失败，请重试")
            }
            HttpResult.Error(e)
        }
        when (response) {
            is HttpResult.Success -> {

                val data = response.result.data

                val hasNotNext = (data!=null)&&(data.size< it.loadSize) && (data.over)

                PagingSource.LoadResult.Page(
                    data = data?.datas?: emptyList(),
                    prevKey = if (page - 1 > 0) page - 1 else null,
                    nextKey = if (hasNotNext) null else page + 1
                )
            }
            is HttpResult.Error -> {
                PagingSource.LoadResult.Error(response.exception)
            }
        }
    }
}
```

譬如在广场这个选项卡页面的 `viewModule` 我们就可以这样子写完整个分页请求：

```kotlin
@HiltViewModel
class WanAndroidSquareViewModule @Inject constructor(
    private var service: WanAndroidNetService,
) : ViewModel() {
    private val pager by lazy {
        easyPager {
            delay(2000)
            service.getSquareData(it)
        }.cachedIn(viewModelScope)
    }
    var viewStates by mutableStateOf(SquareViewState(pagingData = pager))
        private set
}
//描述当前的分页状态并告知页面显示
data class SquareViewState(
    val isRefreshing: Boolean = false,
    val listState: LazyListState = LazyListState(),
    val pagingData: PagingArticle
)

sealed class SquareViewAction {
    object Refresh : SquareViewAction()
}
```

基于 `lottie` 动画库结合 `SwipeRefresh` 实现刷新及加载更多的封装。核心代码如下：

```kotlin
@Composable
fun <T : Any> RefreshWidget(
    lazyPagingItems: LazyPagingItems<T>,
    isRefreshing: Boolean = false,
    modifier: Modifier = Modifier,
    onRefresh: (() -> Unit) = {},
    listState: LazyListState = rememberLazyListState(),
    itemContent: LazyListScope.() -> Unit,
) {
    val rememberSwipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)
    //错误页
    val err = lazyPagingItems.loadState.refresh is LoadState.Error
    if (err) {
        ErrorContent { lazyPagingItems.retry() }
        return
    }
    SwipeRefresh(
        state = rememberSwipeRefreshState,
        onRefresh = {
            onRefresh.invoke()
            lazyPagingItems.refresh()
        },
        modifier = modifier,
        indicator = {state, refreshTrigger ->RefreshWidgetIndicator(state, refreshTrigger)  }
    ) {
        //刷新状态
        rememberSwipeRefreshState.isRefreshing =
            ((lazyPagingItems.loadState.refresh is LoadState.Loading) || isRefreshing)
        //列表
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            state = listState
        ) {
            //条目布局
            itemContent()
            //加载更多状态：加载中和加载错误,没有更多
            if (!rememberSwipeRefreshState.isRefreshing) {
                item {
                    lazyPagingItems.apply {
                        when (loadState.append) {
                            is LoadState.Loading -> LoadingItem()
                            is LoadState.Error -> ErrorItem { retry() }
                            is LoadState.NotLoading -> {
                                if (loadState.append.endOfPaginationReached) {
                                    NoMoreItem()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

```

最终可实现到以下效果：

<img src=".\document\l_model.gif"/>

## Day 4

掌握一些开库库譬如：`coil`、`systemuicontroller`、`datastore` 等使用，基于这样譬如是实现一个登录页面的核心逻辑及个人中心页面逻辑

<img src=".\document\login_logout.gif" style="zoom:33%;" />

## Day 5

其实到这里也就是串联整个 `App` 页面跳转及样式调整等较为简单的工作。至此整个 App 基于 `MVI` 及`Compose` 的实现就已完工，其实在整个过程中并不是很难，单存在不少的痛点譬如 `constraintlayout` 的铰链有区别、利用`constraintlayout` 布局特性控制控件填充效果、旧的 `UI` 观念转换。

最后插入一句题外话，最近求职中，总是有遇到一些公司觉得做硬件交互 App 类型的人就不能 App 做得很好看，我只能说见仁见智，五天我能够从一个新的知识点重新写一个 `material` 类型 App 进行一个的默默发声。相反我觉得如果做硬件交互类型出身的工程师会比传统纯写页面的工程师更为优势，因为他们更懂性能优化，往往一个行业级别应用 App ，特别与硬件通信打交道 App 性能要求更为严格因为我们无法要求客户使用性能好的手机还是差的手机，只能尽最大程度的压测反复验证才能得出一份较好的答卷。更特别点的在定制化系统上开发由于系统被阉割还需要做一些特殊操作才能实现常见的功能等困难。

目前本人正在观望机会中如果有兴趣的话可以找我联系；

Emial：luckyrookie.ypz@qq.com

项目地址如下：https://github.com/KilleTom/KTAndroidComposeSImple
