package com.mo.qin

import android.graphics.PointF
import com.mo.android.dp
import com.mo.android.interaction.VibratorServer
import com.mo.base.Color
import com.mo.base.Start
import com.mo.core.Entity
import com.mo.f
import com.mo.loopSwitch
import com.mo.mutual.effect.Shadow
import com.mo.mutual.element.Circle
import com.mo.mutual.element.Empty
import com.mo.mutual.element.Rect
import com.mo.mutual.element.Text
import com.mo.mutual.input.Click
import com.mo.mutual.layout.linear.Column
import com.mo.mutual.layout.space.Space
import com.mo.mutual.shader.GradientShader
import com.mo.mutual.sync

@Start
class StartEntity : Entity {

    override val mutual = Space {
        var showNumber by sync(10)
        var circleColor by sync(Color.RED)
        var showState by sync(0, false)
        var circleSize by sync(1400)

        var verticalPos by sync(0.3f)
        var horizontalPos  by sync( 0.5f)
        Rect()
            .shader(
                GradientShader(
                    GradientShader.GradientType.LINEAR,
                    PointF(0f, 1f), PointF(1f, 0f),
                    Color(0xff00FFEA.toInt()), Color(0xffFF53ED.toInt())
                )
            )
            .size { fill() }
            .reaction(Click {
                VibratorServer.shot()
                showNumber = if (showNumber > 100000) 1 else showNumber * 10
                circleSize = loopSwitch(circleSize, 1400, 2000)
                showState = loopSwitch(showState, 0, 1)
                circleColor = loopSwitch(circleColor,Color.RED,Color.ORANGE,Color.WHITE)
                verticalPos = loopSwitch(verticalPos, 0.3f,0.85f)
                horizontalPos = loopSwitch(horizontalPos, 0.5f,0.15f)
                true
            })

        Circle()
            .position {
                vertical(verticalPos)
                horizontal(horizontalPos)
            }
            .size { set(circleSize) }
            .shader(
                GradientShader(
                    GradientShader.GradientType.LINEAR,
                    PointF(0f, 0f), PointF(1f, 1f),
                    circleColor, Color.TRANSPARENT
                )
            )

        Column(0) {
            if (showState == 1) {
                Text("Hello Mutual!", 30.dp.f)
                    .effect(Shadow(radio = 12f, color = Color(0xffDD2B2B2B.toInt())))
                Empty()
                    .size { set(100) }
            }
            Text("Hello World! $showNumber", 30.dp.f)
                .effect(Shadow(radio = 12f, color = Color(0xffDD2B2B2B.toInt())))
                .shader(Color.BLACK)
        }.position { center() }
    }.size { fill() }


    /**
     * 1 循环怎么优化
     * 2 过于复杂自动分区
     * 3 Space超高性能优化
     * 4 多线程layout优化
     * 5 声明增删
     */

    /**
     * mutual意为<相互>,是程序与用户进行相互反应的单位实体
     * 主要分两类,绘制相互,布局相互
     * 绘制相互均为一种形状,并包含针对形状的一些绘制属性
     *  绘制
     *      形状(方向,圆形,路径,文字)
     *      着色器(颜色,纹理,渐变,调色,维度着色)
     *      效果(阴影,发光,毛玻璃,透镜,滤镜,)
     *      混合(alpha,色彩混合)
     *  占据
     *      尺寸规则
     *  位于
     *      位置规则
     *  轨道
     *      动效配置
     *  行为
     *      反应配置
     *
     */

    /**
     * 1 声明响应式编程和命令更新式兼容写法
     * 2 完全解构的前后端框架设计
     * 3 设计与实现分离的开发思想
     * 4 过程式动画框架<轨道>
     * 5 规则携带,
     */

    /**
     * 编程思想
     * 将设计与实现分离，将想法和现实分离
     * 框架应当屏蔽兼容性，性能，规避方案，取巧方案，只呈现效果
     *
     *
     *
     * mutual
     * 1 兼容声明响应式编程和命令更新式编程,之后会有gui设计
     * 2 mutual构建可以完全独立实现视觉交互动效, 独立不耦合业务
     * 3 全新的动画框架<轨道>,全体自带动画,全可中断动画,高阶连续动画,多维动画
     * 4 高自由度全局风格定制
     * 5 全尺寸自适应,元素规则携带
     */
}


