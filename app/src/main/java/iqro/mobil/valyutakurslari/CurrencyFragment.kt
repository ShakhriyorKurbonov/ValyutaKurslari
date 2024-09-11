package iqro.mobil.valyutakurslari

import android.content.Context
import android.os.Bundle
import android.provider.Telephony.Mms.Rate
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import iqro.mobil.valyutakurslari.database.CurrencyDbManager
import iqro.mobil.valyutakurslari.databinding.FragmentCurrencyBinding
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CurrencyFragment:Fragment() {
    private var _binding:FragmentCurrencyBinding?=null
    private val binding get() = _binding!!
    private lateinit var myDbManager: CurrencyDbManager


    override fun onAttach(context: Context) {
        super.onAttach(context)
        myDbManager=CurrencyDbManager(requireContext())
        myDbManager.onCreate()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding= FragmentCurrencyBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        val dataList = mutableListOf<CurrencyDataItem>()
       val cursor= myDbManager.fitch()
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
            }while (cursor.moveToNext())
        }
        val currencyAdapter=CurrencyAdapter(imageList,dataList)
        binding.recyclerView.apply {
            adapter=currencyAdapter
            layoutManager= LinearLayoutManager(requireContext())
        }




    }
}