package com.nodep.app

import android.content.Context
import java.util.concurrent.ConcurrentHashMap

object Blocklist {
    private val domains = ConcurrentHashMap.newKeySet<String>()

    fun load(context: Context) {
        domains.clear()
        try {
            context.assets.open("blocklist.txt").bufferedReader().useLines { lines ->
                lines.forEach { line ->
                    val d = line.trim().lowercase().removePrefix("www.").trim()
                    if (d.isNotEmpty() && !d.startsWith("#") && '.' in d) domains.add(d)
                }
            }
        } catch (_: Exception) {}
    }

    fun isBlockedHost(host: String): Boolean {
        val h = host.lowercase().removePrefix("www.").trimEnd('.')
        if (h.isEmpty()) return false
        if (domains.contains(h)) return true
        val parts = h.split('.')
        for (i in 0 until parts.size - 1) {
            val suffix = parts.subList(i, parts.size).joinToString(".")
            if (domains.contains(suffix)) return true
        }
        val flat = h.replace(Regex("[^a-z0-9]"), "")
        return BRANDS.any { flat.contains(it) }
    }

    fun isBlockedText(text: String): Boolean {
        val t = text.lowercase()
        if (t.contains("http") || t.contains("www.") || '.' in t) {
            val host = extractHost(t) ?: return BRANDS.any { t.replace(Regex("[^a-z0-9]"), "").contains(it) }
            return isBlockedHost(host)
        }
        val flat = t.replace(Regex("[^a-z0-9]"), "")
        return BRANDS.any { flat.contains(it) }
    }

    fun isBlockedPackage(pkg: String): Boolean {
        val p = pkg.lowercase()
        return BLOCKED_PACKAGES.any { p.contains(it) }
    }

    private fun extractHost(s: String): String? {
        val m = Regex("""(?:https?://)?([a-z0-9][a-z0-9.\-]+\.[a-z]{2,})""", RegexOption.IGNORE_CASE)
            .find(s) ?: return null
        return m.groupValues[1].lowercase().removePrefix("www.")
    }

    fun size() = domains.size

    private val BRANDS = listOf(
        "1win", "1xbet", "1xstavka", "melbet", "mostbet", "parimatch", "fonbet",
        "winline", "vavada", "pokerdom", "casebattle", "hellcase", "keydrop",
        "csgoempire", "csgoroll", "gamdom", "rollbit", "roobet", "duelbits",
        "bcgame", "stake", "jabka", "musor", "datdrop", "farmskins", "skinclub",
        "pinup", "gizbo", "starda", "dragonmoney", "joycasino", "azino777",
        "csgofast", "csgobig", "packdraw", "hypedrop", "leonbet", "betboom"
    )

    private val BLOCKED_PACKAGES = listOf(
        "1xbet", "1xstavka", "melbet", "mostbet", "parimatch", "fonbet", "winline",
        "vavada", "pokerdom", "pinup", "leon", "betcity", "olimp", "baltbet",
        "stake", "rollbit", "roobet", "bc.game", "casino", "betting", "pokerstars"
    )
}
