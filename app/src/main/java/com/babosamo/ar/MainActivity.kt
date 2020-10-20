package com.babosamo.ar

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.ar.core.Anchor
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.BaseArFragment
import com.google.ar.sceneform.ux.TransformableNode

class MainActivity : AppCompatActivity() {

    private lateinit var arFragment: ArFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        arFragment = supportFragmentManager.findFragmentById(R.id.arFragment) as ArFragment

        arFragment.setOnTapArPlaneListener(BaseArFragment.OnTapArPlaneListener { hitResult, plane, motionEvent ->
            val anchor: Anchor = hitResult.createAnchor()

            ModelRenderable.builder()
                .setSource(this, Uri.parse("dic.sfb"))
                .build()
                .thenAccept { addModelToScene(anchor, it) }
                .exceptionally {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                    builder.setMessage(it.localizedMessage)
                        .show()
                    return@exceptionally null
                }

        })
    }


    private fun addModelToScene(anchor: Anchor, it: ModelRenderable?) {

        val anchorNode: AnchorNode = AnchorNode(anchor)
        val transform: TransformableNode = TransformableNode(arFragment.transformationSystem)
        transform.scaleController.maxScale = 0.2f
        transform.scaleController.minScale = 0.1f
        transform.setParent(anchorNode)
        transform.renderable = it
        arFragment.arSceneView.scene.addChild(anchorNode)
        transform.select()
    }
}