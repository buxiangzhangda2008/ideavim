/*
 * Copyright 2003-2023 The IdeaVim authors
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.txt file or at
 * https://opensource.org/licenses/MIT.
 */

package org.jetbrains.plugins.ideavim.action.change

import com.intellij.idea.TestFor
import com.maddyhome.idea.vim.state.mode.Mode
import org.jetbrains.plugins.ideavim.VimTestCase
import org.junit.jupiter.api.Test

class UndoActionTest : VimTestCase() {
  @Test
  fun `test simple undo`() {
    val keys = listOf("dw", "u")
    val before = """
                Lorem Ipsum

                ${c}Lorem ipsum dolor sit amet,
                consectetur adipiscing elit
                Sed in orci mauris.
                Cras id tellus in ex imperdiet egestas.
    """.trimIndent()
    val after = before
    doTest(keys, before, after, Mode.NORMAL())
    val editor = fixture.editor
    kotlin.test.assertFalse(editor.caretModel.primaryCaret.hasSelection())
  }

  @Test
  @TestFor(issues = ["VIM-696"])
  fun `undo after selection`() {
    val keys = listOf("v3eld", "u")
    val before = """
                Lorem Ipsum

                ${c}Lorem ipsum dolor sit amet,
                consectetur adipiscing elit
                Sed in orci mauris.
                Cras id tellus in ex imperdiet egestas.
    """.trimIndent()
    val after = before
    doTest(keys, before, after, Mode.NORMAL())
    kotlin.test.assertFalse(hasSelection())
  }

  @Test
  fun `test undo with count`() {
    val keys = listOf("dwdwdw", "2u")
    val before = """
                Lorem Ipsum

                ${c}Lorem ipsum dolor sit amet,
                consectetur adipiscing elit
                Sed in orci mauris.
                Cras id tellus in ex imperdiet egestas.
    """.trimIndent()
    val after = """
                Lorem Ipsum

                ${c}ipsum dolor sit amet,
                consectetur adipiscing elit
                Sed in orci mauris.
                Cras id tellus in ex imperdiet egestas.
    """.trimIndent()
    doTest(keys, before, after, Mode.NORMAL())
    kotlin.test.assertFalse(hasSelection())
  }

//  @Test
//  @TestFor(issues = ["VIM-308"])
//  fun `test cursor movements do not require additional undo`() {
//    if (!optionsIjNoEditor().oldundo) {
//      val keys = listOf("a1<Esc>ea2<Esc>ea3<Esc>", "uu")
//      val before = """
//                Lorem Ipsum
//
//                ${c}Lorem ipsum dolor sit amet,
//                consectetur adipiscing elit
//                Sed in orci mauris.
//                Cras id tellus in ex imperdiet egestas.
//      """.trimIndent()
//      val after = """
//                Lorem Ipsum
//
//                I1 found$c it in a legendary land
//                consectetur adipiscing elit
//                Sed in orci mauris.
//                Cras id tellus in ex imperdiet egestas.
//      """.trimIndent()
//      doTest(keys, before, after, Mode.NORMAL())
//      kotlin.test.assertFalse(hasSelection())
//    }
//  }

  private fun hasSelection(): Boolean {
    val editor = fixture.editor
    return editor.caretModel.primaryCaret.hasSelection()
  }
}
