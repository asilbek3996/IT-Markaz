package com.example.itcenter.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.itcenter.R
import com.example.itcenter.databinding.ViewholderQuestionBinding

interface score {
    fun amount(number: Double, clickedAnswer: String,rightAnswer: Int,wrongAnswer: Int)
}
class QuestionAdapter(
    val correctAnswer: String,
    val users: MutableList<String> = mutableListOf(),
    val score: Double,
    var returnScore: score
) : RecyclerView.Adapter<QuestionAdapter.Viewholder>() {


    private lateinit var binding: ViewholderQuestionBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionAdapter.Viewholder {
        val inflate = LayoutInflater.from(parent.context)
        binding = ViewholderQuestionBinding.inflate(inflate, parent, false)
        return Viewholder()
    }

    override fun onBindViewHolder(holder: QuestionAdapter.Viewholder, position: Int) {
        val binding = ViewholderQuestionBinding.bind(holder.itemView)
            binding.answerTxt.text = differ.currentList[position]

        if (position == 0) {
            binding.textTxt.text = "A:"
            binding.answerTxt.text = differ.currentList[position]
        } else if (position == 1) {
            binding.textTxt.text = "B:"
            binding.answerTxt.text = differ.currentList[position]
        } else if (position == 2) {
            binding.textTxt.text = "C:"
            binding.answerTxt.text = differ.currentList[position]
        } else if (position == 3) {
            binding.textTxt.text = "D:"
            binding.answerTxt.text = differ.currentList[position]
        }
        var currentPos = 0
        when (correctAnswer) {
            "a" -> {
                currentPos = 0
            }

            "b" -> {
                currentPos = 1
            }

            "c" -> {
                currentPos = 2
            }

            "d" -> {
                currentPos = 3
            }
        }

        if (differ.currentList.size == 5 && currentPos == position) {
            binding.answerTxt.setBackgroundResource(R.drawable.green_background)
            binding.answerTxt.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.white
                )
            )
        }

        if (differ.currentList.size == 5) {
            var clickedPos = 0
            when (differ.currentList[4]) {
                "a" -> {
                    clickedPos = 0
                }

                "b" -> {
                    clickedPos = 1
                }

                "c" -> {
                    clickedPos = 2
                }

                "d" -> {
                    clickedPos = 3
                }
            }
            if (clickedPos == position && clickedPos != currentPos) {
                binding.answerTxt.setBackgroundResource(R.drawable.red_background)
                binding.answerTxt.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.white
                    )
                )
            }
        }
        if (position == 4) {
            binding.root.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            var str = ""
            when (position) {
                0 -> {
                    str = "a"
                }

                1 -> {
                    str = "b"
                }

                2 -> {
                    str = "c"
                }

                3 -> {
                    str = "d"
                }
            }

            users.add(4, str)
            notifyDataSetChanged()

            if (currentPos == position) {
                binding.answerTxt.setBackgroundResource(R.drawable.green_background)
                binding.answerTxt.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.white
                    )
                )
                returnScore.amount(score, str,1,0)
            } else {
                binding.answerTxt.setBackgroundResource(R.drawable.red_background)
                binding.answerTxt.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.white
                    )
                )
                returnScore.amount(0.0, str,0,1)
            }
        }
        if (differ.currentList.size==5) holder.itemView.setOnClickListener(null)
    }

    override fun getItemCount() = differ.currentList.size

    inner class Viewholder : RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
}