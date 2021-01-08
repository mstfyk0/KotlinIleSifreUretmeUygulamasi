package com.mustafayigitkarakoca.sifreuretmeuygulamasi


import android.app.AlertDialog
import android.content.*
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.recycler_row.view.*

class ListeRecyclerAdapter(val sifreIdListesi: ArrayList<Int> ,val sifreNereninSifresi: ArrayList<String>,val sifreListesi: ArrayList<String>) :RecyclerView.Adapter<ListeRecyclerAdapter.SifreHolder>() {
    class SifreHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SifreHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recycler_row, parent, false)
        return SifreHolder(view)
    }
    override fun onBindViewHolder(holder: SifreHolder, position: Int) {
        holder.itemView.textViewNereninSifresiRecyclerView.text = sifreNereninSifresi[position]
        holder.itemView.textViewSifreRecyclerView.text = sifreListesi[position]
        holder.itemView.textViewSifreRecyclerView.setOnClickListener {
            val uyariMesaji = AlertDialog.Builder(it.context)
            uyariMesaji.setTitle("İŞLEM")
            uyariMesaji.setMessage("NE İŞLEM YAPMAK İSTİYORSUNUZ?")
            uyariMesaji.setPositiveButton("DEĞİŞİKLİK", DialogInterface.OnClickListener { dialog, i ->
                val action = KaydedilenSifrelerDirections.actionKaydedilenSifrelerToSifreUretmeFragment("recyclerdangeldim", sifreIdListesi[position])
                Navigation.findNavController(it).navigate(action)
            })
            uyariMesaji.setNeutralButton("SİLMEK") { dialog, i ->
                val activity : MainActivity = it.context as MainActivity
                activity.finishMe()
                val secilenId = sifreIdListesi[position]
                val databaseSilme = it.context.openOrCreateDatabase("Sifreler", Context.MODE_PRIVATE, null)
                databaseSilme.execSQL("DELETE FROM sifreler WHERE id = ?", arrayOf(secilenId))
                val intent = Intent(holder.itemView.context,MainActivity::class.java)
                holder.itemView.context.startActivity(intent)
            }
            uyariMesaji.setNegativeButton("KOPYALAMA", DialogInterface.OnClickListener { dialog2, i2 ->
                val clipboard = it.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val textToCopy = holder.itemView.textViewSifreRecyclerView.text
                val clip = ClipData.newPlainText("", textToCopy)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(it.context, "ŞİFRE KOPYALANDI", Toast.LENGTH_LONG).show()
            })
            uyariMesaji.show()
        }
    }
    override fun getItemCount(): Int {
        return sifreListesi.size
    }
}