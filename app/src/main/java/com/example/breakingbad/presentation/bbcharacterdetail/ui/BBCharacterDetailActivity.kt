package com.example.breakingbad.presentation.bbcharacterdetail.ui

import android.content.Context
import android.content.Intent
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import com.example.breakingbad.R
import com.example.breakingbad.databinding.ActivityBbCharacterDetailBinding
import com.example.breakingbad.databinding.BbCharacterDetailViewBinding
import com.example.breakingbad.domain.BBCharacter
import com.example.breakingbad.framework.base.BaseActivity
import com.example.breakingbad.framework.extensions.linkSpanned
import com.example.breakingbad.framework.extensions.populateWithGlide
import com.example.breakingbad.framework.extensions.setTextOrUnknown
import com.example.breakingbad.presentation.bbcharacterdetail.viewmodel.BBCharacterDetailViewModel

class BBCharacterDetailActivity : BaseActivity() {

    private lateinit var binding: ActivityBbCharacterDetailBinding
    private lateinit var detailBinding: BbCharacterDetailViewBinding
    private lateinit var viewModel: BBCharacterDetailViewModel

    companion object {
        private const val EXTRA_ITEM_ID = "item_id"

        fun Context.startCatDetailActivity(itemId: Int) {
            this.startActivity(createIntent(this, itemId))
        }

        private fun createIntent(
            context: Context,
            itemId: Int
        ): Intent {
            return Intent(context, BBCharacterDetailActivity::class.java)
                .apply { putExtra(EXTRA_ITEM_ID, itemId) }
        }
    }

    override fun initialise() {
        super.initialise()
        fetchLocalItem()
    }

    override fun observeLiveData() {
        viewModel.model.observe(this, {
            populate(it)
        })
    }

    override fun initialiseViewModel() {
        viewModel = ViewModelProviders
            .of(this, viewModelFactory)[BBCharacterDetailViewModel::class.java]
    }

    override fun initialiseViewBinding() {
        binding = ActivityBbCharacterDetailBinding.inflate(layoutInflater)
        detailBinding = binding.itemDetailContainer
    }

    override fun getRootView(): View {
        return binding.root
    }

    override fun getToolbar(): Toolbar {
        return binding.itemDetailToolbar
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun fetchLocalItem() {
        if (intent.hasExtra(EXTRA_ITEM_ID)) {
            val selectedItemId = intent.extras?.getInt(EXTRA_ITEM_ID)
            selectedItemId?.let {
                viewModel.retrieveItemById(it)
            }
        }
    }

    private fun populate(item: BBCharacter) {
        this.populateWithGlide(detailBinding.img, item.img)
        detailBinding.txtName.setTextOrUnknown(item.name)
        detailBinding.txtBirthday.setTextOrUnknown(item.birthday)
        detailBinding.txtStatus.setTextOrUnknown(item.status)
        detailBinding.txtNickname.setTextOrUnknown(item.nickname)
        detailBinding.txtPortrayed.setTextOrUnknown(item.portrayed)
        detailBinding.txtCategory.setTextOrUnknown(item.category.toString())
        detailBinding.txtImgUrl.movementMethod = LinkMovementMethod.getInstance()
        detailBinding.txtImgUrl.text = linkSpanned(
            getString(R.string.missing_img_url),
            getString(R.string.character_image),
            item.img
        )
    }

}
