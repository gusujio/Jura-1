
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView

import com.example.jura20.R
import com.example.jura20.fragments.HomeFragment.DetailFragment
import com.example.jura20.fragments.HomeFragment.data.DataStorage
import com.example.jura20.fragments.HomeFragment.JurAdapter
import com.example.jura20.fragments.HomeFragment.OnItemClickListener
import com.example.jura20.fragments.HomeFragment.data.Item


class HomeFragment : Fragment(), OnItemClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val data = DataStorage()
        val list = data.getList()
        val rv = root.findViewById<RecyclerView>(R.id.my_recycler_view)
        rv.adapter = JurAdapter( context, this)
        rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL))
        val adapter = rv.adapter as JurAdapter
        adapter.submitList(list)
        return root
    }


    override fun click(item: Item, position: Int) {
        val newFragment = DetailFragment.newInstance(item)
        (activity as FragmentActivity).supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.container, newFragment)
            .commit()
    }

}
