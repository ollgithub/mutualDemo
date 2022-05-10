package com.mo.app

import android.graphics.PointF
import com.mo.base.Color
import com.mo.core.LastEntity
import com.mo.loopSwitch
import com.mo.mutual.Mutual
import com.mo.mutual.effect.Shadow
import com.mo.mutual.element.Rect
import com.mo.mutual.element.Text
import com.mo.mutual.input.Click
import com.mo.mutual.layout.space.Space
import com.mo.mutual.shader.GradientShader
import com.mo.mutual.sync

class ToolEntity() : LastEntity() {

    var x by sync(0f)

    var color by sync(Color.RED)


    override val mutual: Mutual = Space {
        Rect()
            .shader(
                GradientShader(
                    GradientShader.GradientType.LINEAR,
                    PointF(0f, 0f),
                    PointF(x, 1f),
                    color,
                    Color.TRANSPARENT
                )
//            ColorShader(Color.WHITE)
            )
            .size { set(width, 600) }
            .reaction(Click {
                color = loopSwitch(color, Color.WHITE, Color.ORANGE, Color.LTGRAY)
                true
            })
        Text(
            string = "Hello World",
            size = 80f
        )
            .shader(Color.WHITE)
            .effect(Shadow())
            .position {
                vertical(0.4f)
                horizontal(0.15f)
            }
            .reaction(Click {
                color = loopSwitch(color, Color.WHITE, Color.ORANGE, Color.LTGRAY)
                true
            })
        Text(
            string = "Hello Mutual",
            size = 80f
        )
            .shader(Color.WHITE)
            .effect(Shadow())
            .position {
                vertical(0.6f)
                horizontal(0.15f)
            }
    }


}