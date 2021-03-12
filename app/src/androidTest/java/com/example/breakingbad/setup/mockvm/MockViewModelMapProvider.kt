package com.example.breakingbad.setup.mockvm

import androidx.lifecycle.ViewModel
import com.example.breakingbad.presentation.bbcharacterdetail.viewmodel.BBCharacterDetailViewModel
import com.example.breakingbad.presentation.home.viewmodel.HomeViewModel

import javax.inject.Provider

fun provideMockVmMap(): MutableMap<Class<out ViewModel>, Provider<ViewModel>> {
    val viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>> = mutableMapOf()
    viewModels[HomeViewModel::class.java] = Provider { MockHomeVmProvider.provideMockHomeVm() }
    viewModels[BBCharacterDetailViewModel::class.java] = Provider { MockBBCharacterDetailVmProvider.provideMockBBCharacterDetailVm() }
    return viewModels
}