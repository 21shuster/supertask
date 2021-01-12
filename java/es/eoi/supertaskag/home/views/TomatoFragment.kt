package es.eoi.supertaskag.home.views

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import es.eoi.supertaskag.R
import kotlinx.android.synthetic.main.fragment_tomato.*

class TomatoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tomato, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val timer = object: CountDownTimer(20000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val formatted = "${(millisUntilFinished / 60).toString().padStart(2, '0')} : ${(millisUntilFinished % 60).toString().padStart(2, '0')}"
                tvTimer.text = formatted
            }

            override fun onFinish() {
                tvTimer.text = "00:00"
            }
        }
        timer.start()
    }

}