package nl.rso.axon.happergame.testplaygrounds

val humanSimpleMove = """
                     HX
                     XE
                    """.replace("\\s".toRegex(), "").toList().map({"" + it})

val humanPushTwoBoxes = """
                     XHXXB
                     XBRXX
                     XBRBR
                     XXXXX
                     XBXXX
                     XRXXE
                  """.replace("\\s".toRegex(), "").toList().map({"" + it})


val humanBounced = """
                     XHXXB
                     XBRXX
                     XBRBR
                     XBXXE
                  """.replace("\\s".toRegex(), "").toList().map({"" + it})


val humanWin = """
                     XXXXB
                     XBRHX
                     XBRBR
                     XBXXE
                  """.replace("\\s".toRegex(), "").toList().map({"" + it})

val humanLos = """
                     XXXXB
                     XBRHX
                     XBRER
                     XBXXX
                  """.replace("\\s".toRegex(), "").toList().map({"" + it})

val humanLos2 = """
                     XXXBB
                     XBREB
                     XBRHR
                     XBXXX
                  """.replace("\\s".toRegex(), "").toList().map({"" + it})



val happerMove = """
                     HXXBB
                     XBRBB
                     XBBBB
                     XXXXE
                  """.replace("\\s".toRegex(), "").toList().map({"" + it})