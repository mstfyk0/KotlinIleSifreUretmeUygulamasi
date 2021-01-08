package com.mustafayigitkarakoca.sifreuretmeuygulamasi


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_kaydedilen_sifreler.*
import java.lang.Exception

class KaydedilenSifreler : Fragment() {
    private lateinit var listeAdapter : ListeRecyclerAdapter
    var sifreIdListesi = ArrayList<Int>()
    var sifreNereninListesi = ArrayList<String>()
    var sifreListesi = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kaydedilen_sifreler, container, false)
    }
    override fun onStart() {
        super.onStart()
    }
    override fun onResume() {
        super.onResume()
    }
    override fun onPause() {
        super.onPause()
    }
    override fun onStop() {
        super.onStop()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        listeAdapter = ListeRecyclerAdapter(sifreIdListesi, sifreNereninListesi, sifreListesi)
        recyclerViewKaydedilenSifreler.layoutManager= LinearLayoutManager(context)
        recyclerViewKaydedilenSifreler.adapter=listeAdapter
        sqlVeriAlma()
        //super.onViewCreated(view, savedInstanceState)
    }
    fun sqlVeriAlma(){
        try {
            activity?.let{
                val database =it.openOrCreateDatabase("Sifreler",Context.MODE_PRIVATE,null)
                val cursor =database.rawQuery("SELECT * FROM sifreler",null)
                val sifreIdIndex =cursor.getColumnIndex("id")
                val sifreNereninIndex = cursor.getColumnIndex("sifreninkaydi")
                val sifreIndex = cursor.getColumnIndex("sifre")
                sifreIdListesi.clear()
                sifreNereninListesi.clear()
                sifreListesi.clear()
                while (cursor.moveToNext()){
                    sifreIdListesi.add(cursor.getInt(sifreIdIndex))
                    //println(sifreIdListesi)
                    sifreNereninListesi.add(cursor.getString(sifreNereninIndex))
                    sifreListesi.add(cursor.getString(sifreIndex))
                }
                listeAdapter.notifyDataSetChanged()
                cursor.close()
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}