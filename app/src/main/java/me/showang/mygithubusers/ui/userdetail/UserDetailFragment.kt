package me.showang.mygithubusers.ui.userdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import github.showang.kat.assignN
import me.showang.mygithubusers.databinding.FragmentUserDetailBinding

class UserDetailFragment : Fragment() {

    private var binding: FragmentUserDetailBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentUserDetailBinding.inflate(inflater).assignN(::binding).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.run {

        }
    }


}