package com.example.breakingbad.framework.di

import com.example.breakingbad.framework.base.BaseActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingsModule {

    @ContributesAndroidInjector
    abstract fun baseActivity(): BaseActivity

}
