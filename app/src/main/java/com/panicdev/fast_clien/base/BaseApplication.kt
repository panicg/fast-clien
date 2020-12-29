package com.panicdev.fast_clien.base

import android.app.Application
import android.content.Context
import android.os.Handler
import com.panicdev.fast_clien.viewModel.BottomMenuViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import java.util.*

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        context = this.applicationContext
        applicationHandler = Handler(this.mainLooper)

        startKoin {
            androidContext(this@BaseApplication)
            modules(moduleAPI) //API
            modules(moduleModel) //Model
            modules(moduleViewModel) //ViewModel
        }
    }


    val moduleAPI = module {


        //API
//        single<UserAPI> {
//            Retrofit.Builder().apply {
//                baseUrl(ConstantData.BASE_URL())
//                addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                addConverterFactory(GsonConverterFactory.create())
//                client(client)
//            }.build().create(UserAPI::class.java)
//        }


    }

    val moduleModel = module {
//        factory<UserModel> { UserModelImpl(get()) }
    }

    val moduleViewModel = module {
        /**
         * 기본
         */
        viewModel {
            BottomMenuViewModel()
        }
//        viewModel {
//            TabViewModel2()
//        }
//        viewModel {
//            TabViewModel3()
//        }
//        viewModel {
//            TabViewModel4()
//        }


    }


//    val client: OkHttpClient = OkHttpClient.Builder().apply {
//        val log = HttpLoggingInterceptor()
//        log.level = HttpLoggingInterceptor.Level.BODY
//        addInterceptor(log)
//    }.build()

    companion object {
        lateinit var context: Context
        lateinit var applicationHandler: Handler
        lateinit var locale: Locale

    }
}