package me.showang.mygithubusers.ui.userdetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.URLSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import github.showang.kat.assignN
import me.showang.mygithubusers.databinding.FragmentUserDetailBinding
import me.showang.mygithubusers.model.UserDetail
import me.showang.mygithubusers.model.UserInfo
import org.koin.android.ext.android.inject

class UserDetailFragment : Fragment(), UserDetailPresenter.ViewDelegate {

    private var binding: FragmentUserDetailBinding? = null
    private val args: UserDetailFragmentArgs by navArgs()
    private val presenter: UserDetailPresenter by inject()
    private var userDetail: UserDetail? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentUserDetailBinding.inflate(inflater).assignN(::binding).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.viewDelegate = this
        binding?.run {
            initToolbar()
            initUserLayout()
        }
    }

    override fun onDestroyView() {
        presenter.viewDelegate = null
        super.onDestroyView()
    }

    private fun FragmentUserDetailBinding.initToolbar() {
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun FragmentUserDetailBinding.initUserLayout() {
        userDetail?.let {
            updateViewBy(it)
        } ?: args.userInfo?.let {
            initViewBy(it)
            presenter.initialDataBy(it)
        }
    }

    private fun FragmentUserDetailBinding.initViewBy(userInfo: UserInfo) {
        Glide.with(this@UserDetailFragment)
            .load(userInfo.avatarUrl)
            .circleCrop()
            .into(avatarImage)
        accountNameText.text = userInfo.account
        staffBadge.isVisible = userInfo.isStaff
    }

    private fun FragmentUserDetailBinding.updateViewBy(userDetail: UserDetail) {
        userDisplayNameText.text = userDetail.name
        bioText.text = userDetail.bio
        locationText.text = userDetail.location
        userDetail.blogUrl?.let { url ->
            linkText.setText(
                SpannableString(url).apply {
                    setSpan(
                        URLSpan(""),
                        0,
                        length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                },
                TextView.BufferType.SPANNABLE
            )
            linkText.setOnClickListener {
                try {
                    Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(url)
                    }.run(::startActivity)
                } catch (e: Throwable) {
                    Toast.makeText(requireContext(), "Link can't be open.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    override fun onLoadDetailFailed(cause: Throwable) {
        Toast.makeText(
            requireContext(),
            "Load user detail failed, cause: ${cause.localizedMessage}",
            Toast.LENGTH_SHORT
        ).show()
        findNavController().popBackStack()
    }

    override fun onLoadDetailSuccess(detail: UserDetail) {
        binding?.updateViewBy(detail)
    }

}