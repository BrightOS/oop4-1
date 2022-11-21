package ru.brightos.oop41

import android.annotation.SuppressLint
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import android.view.MotionEvent
import ru.brightos.oop41.databinding.ActivityMainBinding
import ru.brightos.oop41.model.CCircle
import ru.brightos.oop41.utils.correctDeclensionOfDeleted
import ru.brightos.oop41.utils.correctDeclensionOfObjects
import ru.brightos.oop41.utils.dp
import ru.brightos.oop41.utils.extendedListOf
import ru.brightos.oop41.view.CircleView
import ru.brightos.oop41.view.DrawLayout
import ru.brightos.oop41.view.OnSingleObjectSelectedListener
import ru.brightos.oop41.view.SelectableView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val circlesList = extendedListOf<SelectableView>()
    val listener = object : OnSingleObjectSelectedListener {
        override fun onSingleObjectSelected() {
            circlesList.forEach { it.deselect() }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.root.setOnTouchAction { motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                val radius = 30.dp

                val newCircle = CircleView(
                    context = this,
                    attrs = null,
                    circle = CCircle(
                        x = motionEvent.x,
                        y = motionEvent.y,
                        radius = radius
                    ),
                    onSingleObjectSelectedListener = listener
                )

                circlesList.forEach { it.deselect() }
                circlesList.add(newCircle)
                binding.root.addView(newCircle)
                println(binding.root.right)
            }
        }

        binding.delete.setOnClickListener {
            val idsToDelete = extendedListOf<Int>()

            circlesList.forEachIndexed { index, element ->
                if (element.deleteView())
                    idsToDelete.add(index)
            }

            val deletedObjectsCount = idsToDelete.size

            while (!idsToDelete.isEmpty)
                circlesList.removeAt(idsToDelete.popLast()!!)

            Snackbar.make(
                it,
                "${
                    correctDeclensionOfDeleted(deletedObjectsCount)
                } $deletedObjectsCount ${
                    correctDeclensionOfObjects(deletedObjectsCount)
                }", // Удален(о) N объект(а)(ов)
                Snackbar.LENGTH_LONG
            )
                .setAction("Ок") {}
                .setAnchorView(R.id.delete)
                .show()
        }
    }
}
