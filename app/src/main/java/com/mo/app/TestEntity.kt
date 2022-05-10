package com.mo.app

import android.graphics.Bitmap
import com.mo.R
import com.mo.android.assist.bitmap
import com.mo.core.Entity
import com.mo.mutual.Mutual
import com.mo.mutual.layout.space.Space


class TestEntity : Entity {

    private var background: Bitmap = R.drawable.background.bitmap()

    override val mutual: Mutual = Space {
        // 以下声明14ms
//        repeat(1000){
//            Bitmap(background)
//                .markId(R.id.t2)
//                .blend(0.5f)
//                .size {
//                    fitShortest()
//                }
//                .position { right() }
//                .reaction(Click {
//                    true
//                })
//        }

        // 以下声明6ms
//        repeat(1000){
//            Bitmap(background)
//        }


        // 以下声明10ms-13ms
//        repeat(1000) {
//        Bitmap(background)
//            .size { fitLongest() }
//            .position {
//                center()
//            }
//        }
    }.size { }

}