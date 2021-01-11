import android.content.Context
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jura20.R
import com.example.jura20.fragments.HomeFragment.data.Text

class SwipeAdapter(var context: Context?, var id: Int) : RecyclerView.Adapter<SwipeAdapter.ViewPager>() {
    var list: ArrayList<String> = Text().getText(id)
    class ViewPager(itemView: View): RecyclerView.ViewHolder(itemView)
    var pagePosition: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPager {
        return  ViewPager(LayoutInflater.from(parent.context).inflate(R.layout.fragment_swipe, parent, false))
    }


    override fun onBindViewHolder(holder: ViewPager, position: Int) {
        val textView = holder.itemView.findViewById<TextView>(R.id.text_swipe)
        if(list[position].contains("<font")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                textView?.setText(
                    Html.fromHtml(list[position], Html.FROM_HTML_MODE_LEGACY),
                    TextView.BufferType.SPANNABLE
                )
            } else {
                textView?.setText(Html.fromHtml(list[position]), TextView.BufferType.SPANNABLE)
            }
        }
        else{
            textView.text = Text().getText(id)[position]
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


}
//class SwipeAdapter(fm: FragmentManager, var id: Int) : FragmentStatePagerAdapter(fm){
//
//    var pagePosition: Int = 0
//
//    override fun getCount(): Int {
//        val text = Text()
//        return text.getText(id).size;
//    }
//
//    override fun getItem(position: Int): Fragment {
//        val fragment = SwipeFragment()
//        val bundle = Bundle()
//        val text = Text()
//        pagePosition = position
//        bundle.putString("text", text.getText(id)[position])
//        fragment.arguments = (bundle)
//        return fragment
//    }
//
//    fun updateSetTextColor() {
//        val map = mapOf("absolutely" to "red", "declaration" to "green", "room" to "red")
//        val textView = getItem(pagePosition). .findViewById<TextView>(R.id.text_swipe)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            textView?.setText(Html.fromHtml(getFormattedHtmlText(map),  Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.SPANNABLE);
//        } else {
//            textView?.setText(Html.fromHtml(getFormattedHtmlText(map)), TextView.BufferType.SPANNABLE);
//        }
//    }
//    private fun getFormattedHtmlText(stringMap: Map<String, String>): String? {
//        val stringBuilder = StringBuilder()
//        for ((key, value) in stringMap) {
//            if (value == "red") {
//                stringBuilder.append("<font color='red'>").append(key).append(" ")
//                    .append("</font>")
//            } else {
//                stringBuilder.append(key).append(" ")
//            }
//        }
//        return stringBuilder.toString()
//    }
//
//}