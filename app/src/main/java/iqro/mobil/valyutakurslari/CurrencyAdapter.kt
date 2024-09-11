package iqro.mobil.valyutakurslari

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import iqro.mobil.valyutakurslari.databinding.CurrencyItemsBinding
import kotlin.math.abs

class CurrencyAdapter(val imageList: List<Int>,val list: List<CurrencyDataItem>):Adapter<CurrencyAdapter.CurrencyViewHolder>() {
    inner class CurrencyViewHolder(val binding:CurrencyItemsBinding):ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
         fun bind(image:Int, item: CurrencyDataItem){
            val diff=item.Diff.toDouble()
            if (diff>0){
                binding.imageView2.setImageResource(R.drawable.arrow_right)
            }else if (diff<0){
                binding.imageView2.setImageResource(R.drawable.arrow_right_1)
            }else{
                binding.imageView2.setImageResource(R.drawable.minus)
            }
            val percent=diff*100/item.Rate.toDouble()
            binding.image.setImageResource(image)
            binding.ccyTv.text=item.Nominal+" "+item.Ccy
            binding.ccyUzTv.text=item.CcyNm_UZ
            binding.rateTv.text=item.Rate
            binding.persentTv.text=String.format("%.2f", abs(percent))+"%"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        return CurrencyViewHolder(CurrencyItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
       holder.bind(imageList[position],list[position])
    }
}