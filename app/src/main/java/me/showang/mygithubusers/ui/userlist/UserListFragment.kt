package me.showang.mygithubusers.ui.userlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import github.showang.kat.assignN
import github.showang.transtate.core.Transform
import github.showang.transtate.core.ViewEvent
import github.showang.transtate.core.ViewState
import me.showang.mygithubusers.R
import me.showang.mygithubusers.databinding.FragmentUserListBinding
import me.showang.mygithubusers.model.UserInfo
import me.showang.mygithubusers.ui.userdetail.UserDetailFragmentArgs
import me.showang.mygithubusers.util.NavControllerHelper
import me.showang.recyct.RecyctAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserListFragment : Fragment() {

    private val viewModel: UserListViewModel by viewModel()
    private val userInfoList: MutableList<UserInfo> = mutableListOf()
    private val navHelper: NavControllerHelper = NavControllerHelper()
    private var binding: FragmentUserListBinding? = null
    private var adapter: RecyctAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentUserListBinding.inflate(inflater).assignN(::binding).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.run {
            initUserRecycler()
            retryButton.setOnClickListener {
                viewModel.retry()
            }
        }
        viewModel.observeTransformation(viewLifecycleOwner, ::initViewBy, ::updateViewBy)
    }

    override fun onResume() {
        super.onResume()
        navHelper.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        adapter = null
    }

    private fun onUserInfoItemClick(userInfo: UserInfo) {
        val directions = UserListFragmentDirections.actionUserListToDetail(userInfo)
        navHelper.navigate(findNavController(), directions)
    }

    private fun initViewBy(state: ViewState) {
        binding?.run {
            when (state) {
                is UserListViewModel.State.Initializing -> {
                    progress.isVisible = true
                    errorLayout.isVisible = false
                }
                is UserListViewModel.State.DataLoaded -> {
                    progress.isVisible = false
                    errorLayout.isVisible = false
                    userInfoList.addAll(state.userInfoList)
                    adapter?.enableLoadMore = state.hasNextPage
                }
                is UserListViewModel.State.ErrorState -> {
                    progress.isVisible = false
                    messageText.text = state.error.localizedMessage
                    errorLayout.isVisible = true
                }
            }
        }
    }

    private fun updateViewBy(transform: Transform) {
        when (transform.fromState) {
            is UserListViewModel.State.DataLoaded -> updateLoadedStateBy(transform.byEvent)
            else -> initViewBy(transform.newState)
        }
    }

    private fun updateLoadedStateBy(event: ViewEvent) {
        when (event) {
            // Update new page items by notify item change
            is UserListViewModel.Event.LoadDataSuccess -> {
                val preSize = userInfoList.size
                val headerSize = 1
                userInfoList.addAll(event.newUserInfoList)
                adapter?.run {
                    enableLoadMore = event.hasNextPage
                    // Update load more item
                    adapter?.notifyItemChanged(preSize + headerSize)
                    // Insert new items
                    adapter?.notifyItemRangeInserted(
                        preSize + headerSize,
                        event.newUserInfoList.size - 1
                    )
                }
            }
            is UserListViewModel.Event.LoadDataFailed -> {
                adapter?.isLoadMoreFail = true
                adapter?.notifyItemChanged(userInfoList.size)
            }
        }
    }

    private fun FragmentUserListBinding.initUserRecycler() {
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = RecyctAdapter(userInfoList).apply {
            registerHeader(HeaderItem())
            register(UserInfoItem()) { data, _ ->
                (data as? UserInfo)?.let(::onUserInfoItemClick)
            }
            defaultLoadMore(viewModel::loadNextPage)
        }.assignN(::adapter)
    }

}