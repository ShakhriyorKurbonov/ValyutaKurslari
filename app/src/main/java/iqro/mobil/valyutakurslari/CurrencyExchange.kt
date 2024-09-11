package iqro.mobil.valyutakurslari

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener
import iqro.mobil.valyutakurslari.database.CurrencyDbManager
import iqro.mobil.valyutakurslari.databinding.FagmentCalcBinding
import kotlin.time.times

@Suppress("CAST_NEVER_SUCCEEDS")
class CurrencyExchange:Fragment() {
    private var _binding:FagmentCalcBinding?=null
    private val binding get() = _binding!!
    private lateinit var myDbManager: CurrencyDbManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myDbManager=CurrencyDbManager(context)
        myDbManager.onCreate()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val cursor=myDbManager.fitch()
        val dataList= mutableListOf<CurrencyDataItem>()
        val list= mutableListOf<String>()
        if (cursor!=null){
                val idIndex=cursor.getColumnIndex("Id")
                val CcyIndex=cursor.getColumnIndex("Ccy")
                val DateIndex=cursor.getColumnIndex("Date")
                val DiffIndex=cursor.getColumnIndex("Diff")
                val RateIndex=cursor.getColumnIndex("Rate")
                val NormalIndex=cursor.getColumnIndex("Normal")
                val CcyNm_UzIndex=cursor.getColumnIndex("CcyNm_UZ")
                do {
                    val Ccy= cursor.getString(CcyIndex)
                    val CcyNm= cursor.getString(CcyNm_UzIndex)
                    val Date= cursor.getString(DateIndex)
                    val Diff= cursor.getString(DiffIndex)
                    val Rate= cursor.getString(RateIndex)
                    val Normal= cursor.getString(NormalIndex)
                    val id= cursor.getInt(idIndex)
                    dataList.add(CurrencyDataItem(Ccy,null,null,CcyNm,null,null,Date,Diff,Normal,Rate,id))
                    list.add(Ccy)
            }while (cursor.moveToNext())
        }


        val imageList = listOf<Int>(
            R.drawable.us,
            R.drawable.euro,
            R.drawable.russia,
            R.drawable.uk,
            R.drawable.japan,
            R.drawable.azerbaijan,
            R.drawable.bangladesh,
            R.drawable.bulgarian,
            R.drawable.bahrain,
            R.drawable.brunei,
            R.drawable.brazilian,
            R.drawable.belarusian,
            R.drawable.canadian,
            R.drawable.swiss,
            R.drawable.yuan,
            R.drawable.cuban,
            R.drawable.czech,
            R.drawable.danish,
            R.drawable.algerian,
            R.drawable.egyptian,
            R.drawable.afghani,
            R.drawable.argentine,
            R.drawable.georgian,
            R.drawable.hong_kong,
            R.drawable.hungarian,
            R.drawable.rupiah,
            R.drawable.israeli,
            R.drawable.indian,
            R.drawable.iraqi,
            R.drawable.iran,
            R.drawable.iceland,
            R.drawable.jordanian,
            R.drawable.australian,
            R.drawable.kyrgyzstan,
            R.drawable.riel,
            R.drawable.korean,
            R.drawable.kuwait,
            R.drawable.kazakhstan,
            R.drawable.lao,
            R.drawable.lebanese,
            R.drawable.libyan,
            R.drawable.moroccan,
            R.drawable.moldovan,
            R.drawable.kyat,
            R.drawable.tugrik,
            R.drawable.mexician,
            R.drawable.malaysian,
            R.drawable.norway,
            R.drawable.new_zealand,
            R.drawable.rial_omani,
            R.drawable.philippine,
            R.drawable.pakistan,
            R.drawable.polish,
            R.drawable.qatar,
            R.drawable.romania,
            R.drawable.serbian,
            R.drawable.armenian,
            R.drawable.saudi,
            R.drawable.sudanese,
            R.drawable.swedish,
            R.drawable.singapore,
            R.drawable.syrian,
            R.drawable.baht,
            R.drawable.tajikistan,
            R.drawable.turkmenistan,
            R.drawable.tunistan,
            R.drawable.turkish,
            R.drawable.ukraine,
            R.drawable.uae,
            R.drawable.uruguay,
            R.drawable.bolivar,
            R.drawable.dong,
            R.drawable.sdr,
            R.drawable.yemeni,
            R.drawable.rand
        )


        val adapter=ArrayAdapter<String>(requireContext(),R.layout.spinner_item,list)
        binding.spinner.adapter=adapter


        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                binding.imageView4.setImageResource(imageList[position])
                binding.text3.text = "${dataList[position].Nominal} ${dataList[position].Ccy} = ${dataList[position].Rate}"
                val pos = position
                binding.otherEt.isEnabled = false
                binding.uzsEt.isEnabled = true
                binding.otherEt.setText("0.00")
                binding.uzsEt.setText("0.00")

                var c = 0



                binding.imageView3.setOnClickListener {
                    binding.imageView3.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.rotate))
                    c++
                    if (c % 2 == 0) {
                        binding.otherEt.isEnabled = false
                        binding.uzsEt.isEnabled = true
                        binding.textView.text="Miqdori"
                        binding.textView4.text="Konvertatsiya qilingan miqdor"
                    } else {
                        binding.otherEt.isEnabled = true
                        binding.uzsEt.isEnabled = false
                        binding.textView.text="Konvertatsiya qilingan miqdor"
                        binding.textView4.text="Miqdori"
                    }
                    binding.otherEt.setText("0.00")
                    binding.uzsEt.setText("0.00")
                }



                binding.uzsEt.setOnKeyListener { v, keyCode, event ->
                    if (event.action == KeyEvent.ACTION_UP || keyCode == KeyEvent.KEYCODE_ENTER) {
                        val a = binding.uzsEt.text.toString().toDoubleOrNull() ?: 0.0
                        val b = 1.0 / (dataList[pos].Rate.toDouble() / dataList[pos].Nominal.toDouble())
                        val t = a * b
                        binding.otherEt.setText(String.format("%.2f", t))
                        true
                    } else {
                        false
                    }
                }

                binding.otherEt.setOnKeyListener { v, keyCode, event ->
                    if (event.action == KeyEvent.ACTION_UP || keyCode == KeyEvent.KEYCODE_ENTER) {
                        val a = binding.otherEt.text.toString().toDoubleOrNull() ?: 0.0
                        val b = (dataList[pos].Rate.toDouble() / dataList[pos].Nominal.toDouble())
                        val t = a * b
                        binding.uzsEt.setText(String.format("%.2f", t))
                        true
                    } else {
                        false
                    }
                }


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }



    }




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         super.onCreateView(inflater, container, savedInstanceState)
        _binding=FagmentCalcBinding.inflate(layoutInflater)
        return binding.root
    }
}