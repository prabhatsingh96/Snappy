package app.android.snappay.util

import android.util.Log
import app.android.snappay.R
import app.android.snappay.model.bean.CurrencyBean
import java.util.*

val String.getFlagMasterResID: Int
    get() = when (this.toLowerCase()) {
        "ad" //andorra
        -> R.drawable.flag_andorra
        "ae" //united arab emirates
        -> R.drawable.flag_uae
        "af" //afghanistan
        -> R.drawable.flag_afghanistan
        "ag" //antigua & barbuda
        -> R.drawable.flag_antigua_and_barbuda
        "ai" //anguilla // Caribbean Islands
        -> R.drawable.flag_anguilla
        "al" //albania
        -> R.drawable.flag_albania
        "am" //armenia
        -> R.drawable.flag_armenia
        "ao" //angola
        -> R.drawable.flag_angola
        "aq" //antarctica // custom
        -> R.drawable.flag_antarctica
        "ar" //argentina
        -> R.drawable.flag_argentina
        "at" //austria
        -> R.drawable.flag_austria
        "au" //australia
        -> R.drawable.flag_australia
        "aw" //aruba
        -> R.drawable.flag_aruba
        "az" //azerbaijan
        -> R.drawable.flag_azerbaijan
        "ba" //bosnia and herzegovina
        -> R.drawable.flag_bosnia
        "bb" //barbados
        -> R.drawable.flag_barbados
        "bd" //bangladesh
        -> R.drawable.flag_bangladesh
        "be" //belgium
        -> R.drawable.flag_belgium
        "bf" //burkina faso
        -> R.drawable.flag_burkina_faso
        "bg" //bulgaria
        -> R.drawable.flag_bulgaria
        "bh" //bahrain
        -> R.drawable.flag_bahrain
        "bi" //burundi
        -> R.drawable.flag_burundi
        "bj" //benin
        -> R.drawable.flag_benin
        "bl" //saint barthélemy
        -> R.drawable.flag_saint_barthelemy// custom
        "bm" //bermuda
        -> R.drawable.flag_bermuda
        "bn" //brunei darussalam // custom
        -> R.drawable.flag_brunei
        "bo" //bolivia, plurinational state of
        -> R.drawable.flag_bolivia
        "br" //brazil
        -> R.drawable.flag_brazil
        "bs" //bahamas
        -> R.drawable.flag_bahamas
        "bt" //bhutan
        -> R.drawable.flag_bhutan
        "bw" //botswana
        -> R.drawable.flag_botswana
        "by" //belarus
        -> R.drawable.flag_belarus
        "bz" //belize
        -> R.drawable.flag_belize
        "ca" //canada
        -> R.drawable.flag_canada
        "cc" //cocos (keeling) islands
        -> R.drawable.flag_cocos// custom
        "cd" //congo, the democratic republic of the
        -> R.drawable.flag_democratic_republic_of_the_congo
        "cf" //central african republic
        -> R.drawable.flag_central_african_republic
        "cg" //congo
        -> R.drawable.flag_republic_of_the_congo
        "ch" //switzerland
        -> R.drawable.flag_switzerland
        "ci" //côte d\'ivoire
        -> R.drawable.flag_cote_divoire
        "ck" //cook islands
        -> R.drawable.flag_cook_islands
        "cl" //chile
        -> R.drawable.flag_chile
        "cm" //cameroon
        -> R.drawable.flag_cameroon
        "cn" //china
        -> R.drawable.flag_china
        "co" //colombia
        -> R.drawable.flag_colombia
        "cr" //costa rica
        -> R.drawable.flag_costa_rica
        "cu" //cuba
        -> R.drawable.flag_cuba
        "cv" //cape verde
        -> R.drawable.flag_cape_verde
        "cx" //christmas island
        -> R.drawable.flag_christmas_island
        "cy" //cyprus
        -> R.drawable.flag_cyprus
        "cz" //czech republic
        -> R.drawable.flag_czech_republic
        "de" //germany
        -> R.drawable.flag_germany
        "dj" //djibouti
        -> R.drawable.flag_djibouti
        "dk" //denmark
        -> R.drawable.flag_denmark
        "dm" //dominica
        -> R.drawable.flag_dominica
        "do" //dominican republic
        -> R.drawable.flag_dominican_republic
        "dz" //algeria
        -> R.drawable.flag_algeria
        "ec" //ecuador
        -> R.drawable.flag_ecuador
        "ee" //estonia
        -> R.drawable.flag_estonia
        "eg" //egypt
        -> R.drawable.flag_egypt
        "er" //eritrea
        -> R.drawable.flag_eritrea
        "es" //spain
        -> R.drawable.flag_spain
        "et" //ethiopia
        -> R.drawable.flag_ethiopia
        "fi" //finland
        -> R.drawable.flag_finland
        "fj" //fiji
        -> R.drawable.flag_fiji
        "fk" //falkland islands (malvinas)
        -> R.drawable.flag_falkland_islands
        "fm" //micronesia, federated states of
        -> R.drawable.flag_micronesia
        "fo" //faroe islands
        -> R.drawable.flag_faroe_islands
        "fr" //france
        -> R.drawable.flag_france
        "ga" //gabon
        -> R.drawable.flag_gabon
        "gb" //united kingdom
        -> R.drawable.flag_united_kingdom
        "gd" //grenada
        -> R.drawable.flag_grenada
        "ge" //georgia
        -> R.drawable.flag_georgia
        "gf" //guyane
        -> R.drawable.flag_guyane
        "gh" //ghana
        -> R.drawable.flag_ghana
        "gi" //gibraltar
        -> R.drawable.flag_gibraltar
        "gl" //greenland
        -> R.drawable.flag_greenland
        "gm" //gambia
        -> R.drawable.flag_gambia
        "gn" //guinea
        -> R.drawable.flag_guinea
        "gp" //guadeloupe
        -> R.drawable.flag_guadeloupe
        "gq" //equatorial guinea
        -> R.drawable.flag_equatorial_guinea
        "gr" //greece
        -> R.drawable.flag_greece
        "gt" //guatemala
        -> R.drawable.flag_guatemala
        "gw" //guinea-bissau
        -> R.drawable.flag_guinea_bissau
        "gy" //guyana
        -> R.drawable.flag_guyana
        "hk" //hong kong
        -> R.drawable.flag_hong_kong
        "hn" //honduras
        -> R.drawable.flag_honduras
        "hr" //croatia
        -> R.drawable.flag_croatia
        "ht" //haiti
        -> R.drawable.flag_haiti
        "hu" //hungary
        -> R.drawable.flag_hungary
        "id" //indonesia
        -> R.drawable.flag_indonesia
        "ie" //ireland
        -> R.drawable.flag_ireland
        "il" //israel
        -> R.drawable.flag_israel
        "im" //isle of man
        -> R.drawable.flag_isleof_man // custom
        "is" //Iceland
        -> R.drawable.flag_iceland
        "in" //india
        -> R.drawable.flag_india
        "iq" //iraq
        -> R.drawable.flag_iraq_new
        "ir" //iran, islamic republic of
        -> R.drawable.flag_iran
        "it" //italy
        -> R.drawable.flag_italy
        "jm" //jamaica
        -> R.drawable.flag_jamaica
        "jo" //jordan
        -> R.drawable.flag_jordan
        "jp" //japan
        -> R.drawable.flag_japan
        "ke" //kenya
        -> R.drawable.flag_kenya
        "kg" //kyrgyzstan
        -> R.drawable.flag_kyrgyzstan
        "kh" //cambodia
        -> R.drawable.flag_cambodia
        "ki" //kiribati
        -> R.drawable.flag_kiribati
        "km" //comoros
        -> R.drawable.flag_comoros
        "kn" //st kitts & nevis
        -> R.drawable.flag_saint_kitts_and_nevis
        "kp" //north korea
        -> R.drawable.flag_north_korea
        "kr" //south korea
        -> R.drawable.flag_south_korea
        "kw" //kuwait
        -> R.drawable.flag_kuwait
        "ky" //Cayman_Islands
        -> R.drawable.flag_cayman_islands
        "kz" //kazakhstan
        -> R.drawable.flag_kazakhstan
        "la" //lao people\'s democratic republic
        -> R.drawable.flag_laos
        "lb" //lebanon
        -> R.drawable.flag_lebanon
        "lc" //st lucia
        -> R.drawable.flag_saint_lucia
        "li" //liechtenstein
        -> R.drawable.flag_liechtenstein
        "lk" //sri lanka
        -> R.drawable.flag_sri_lanka
        "lr" //liberia
        -> R.drawable.flag_liberia
        "ls" //lesotho
        -> R.drawable.flag_lesotho
        "lt" //lithuania
        -> R.drawable.flag_lithuania
        "lu" //luxembourg
        -> R.drawable.flag_luxembourg
        "lv" //latvia
        -> R.drawable.flag_latvia
        "ly" //libya
        -> R.drawable.flag_libya
        "ma" //morocco
        -> R.drawable.flag_morocco
        "mc" //monaco
        -> R.drawable.flag_monaco
        "md" //moldova, republic of
        -> R.drawable.flag_moldova
        "me" //montenegro
        -> R.drawable.flag_of_montenegro// custom
        "mg" //madagascar
        -> R.drawable.flag_madagascar
        "mh" //marshall islands
        -> R.drawable.flag_marshall_islands
        "mk" //macedonia, the former yugoslav republic of
        -> R.drawable.flag_macedonia
        "ml" //mali
        -> R.drawable.flag_mali
        "mm" //myanmar
        -> R.drawable.flag_myanmar
        "mn" //mongolia
        -> R.drawable.flag_mongolia
        "mo" //macao
        -> R.drawable.flag_macao
        "mq" //martinique
        -> R.drawable.flag_martinique
        "mr" //mauritania
        -> R.drawable.flag_mauritania
        "ms" //montserrat
        -> R.drawable.flag_montserrat
        "mt" //malta
        -> R.drawable.flag_malta
        "mu" //mauritius
        -> R.drawable.flag_mauritius
        "mv" //maldives
        -> R.drawable.flag_maldives
        "mw" //malawi
        -> R.drawable.flag_malawi
        "mx" //mexico
        -> R.drawable.flag_mexico
        "my" //malaysia
        -> R.drawable.flag_malaysia
        "mz" //mozambique
        -> R.drawable.flag_mozambique
        "na" //namibia
        -> R.drawable.flag_namibia
        "nc" //new caledonia
        -> R.drawable.flag_new_caledonia// custom
        "ne" //niger
        -> R.drawable.flag_niger
        "ng" //nigeria
        -> R.drawable.flag_nigeria
        "ni" //nicaragua
        -> R.drawable.flag_nicaragua
        "nl" //netherlands
        -> R.drawable.flag_netherlands
        "no" //norway
        -> R.drawable.flag_norway
        "np" //nepal
        -> R.drawable.flag_nepal
        "nr" //nauru
        -> R.drawable.flag_nauru
        "nu" //niue
        -> R.drawable.flag_niue
        "nz" //new zealand
        -> R.drawable.flag_new_zealand
        "om" //oman
        -> R.drawable.flag_oman
        "pa" //panama
        -> R.drawable.flag_panama
        "pe" //peru
        -> R.drawable.flag_peru
        "pf" //french polynesia
        -> R.drawable.flag_french_polynesia
        "pg" //papua new guinea
        -> R.drawable.flag_papua_new_guinea
        "ph" //philippines
        -> R.drawable.flag_philippines
        "pk" //pakistan
        -> R.drawable.flag_pakistan
        "pl" //poland
        -> R.drawable.flag_poland
        "pm" //saint pierre and miquelon
        -> R.drawable.flag_saint_pierre
        "pn" //pitcairn
        -> R.drawable.flag_pitcairn_islands
        "pr" //puerto rico
        -> R.drawable.flag_puerto_rico
        "ps" //palestine
        -> R.drawable.flag_palestine
        "pt" //portugal
        -> R.drawable.flag_portugal
        "pw" //palau
        -> R.drawable.flag_palau
        "py" //paraguay
        -> R.drawable.flag_paraguay
        "qa" //qatar
        -> R.drawable.flag_qatar
        "re" //la reunion
        -> R.drawable.flag_martinique // no exact flag found
        "ro" //romania
        -> R.drawable.flag_romania
        "rs" //serbia
        -> R.drawable.flag_serbia // custom
        "ru" //russian federation
        -> R.drawable.flag_russian_federation
        "rw" //rwanda
        -> R.drawable.flag_rwanda
        "sa" //saudi arabia
        -> R.drawable.flag_saudi_arabia
        "sb" //solomon islands
        -> R.drawable.flag_soloman_islands
        "sc" //seychelles
        -> R.drawable.flag_seychelles
        "sd" //sudan
        -> R.drawable.flag_sudan
        "se" //sweden
        -> R.drawable.flag_sweden
        "sg" //singapore
        -> R.drawable.flag_singapore
        "sh" //saint helena, ascension and tristan da cunha
        -> R.drawable.flag_saint_helena // custom
        "si" //slovenia
        -> R.drawable.flag_slovenia
        "sk" //slovakia
        -> R.drawable.flag_slovakia
        "sl" //sierra leone
        -> R.drawable.flag_sierra_leone
        "sm" //san marino
        -> R.drawable.flag_san_marino
        "sn" //senegal
        -> R.drawable.flag_senegal
        "so" //somalia
        -> R.drawable.flag_somalia
        "sr" //suriname
        -> R.drawable.flag_suriname
        "st" //sao tome and principe
        -> R.drawable.flag_sao_tome_and_principe
        "sv" //el salvador
        -> R.drawable.flag_el_salvador
        "sx" //sint maarten
        -> R.drawable.flag_sint_maarten
        "sy" //syrian arab republic
        -> R.drawable.flag_syria
        "sz" //swaziland
        -> R.drawable.flag_swaziland
        "tc" //turks & caicos islands
        -> R.drawable.flag_turks_and_caicos_islands
        "td" //chad
        -> R.drawable.flag_chad
        "tg" //togo
        -> R.drawable.flag_togo
        "th" //thailand
        -> R.drawable.flag_thailand
        "tj" //tajikistan
        -> R.drawable.flag_tajikistan
        "tk" //tokelau
        -> R.drawable.flag_tokelau // custom
        "tl" //timor-leste
        -> R.drawable.flag_timor_leste
        "tm" //turkmenistan
        -> R.drawable.flag_turkmenistan
        "tn" //tunisia
        -> R.drawable.flag_tunisia
        "to" //tonga
        -> R.drawable.flag_tonga
        "tr" //turkey
        -> R.drawable.flag_turkey
        "tt" //trinidad & tobago
        -> R.drawable.flag_trinidad_and_tobago
        "tv" //tuvalu
        -> R.drawable.flag_tuvalu
        "tw" //taiwan, province of china
        -> R.drawable.flag_taiwan
        "tz" //tanzania, united republic of
        -> R.drawable.flag_tanzania
        "ua" //ukraine
        -> R.drawable.flag_ukraine
        "ug" //uganda
        -> R.drawable.flag_uganda
        "us" //united states
        -> R.drawable.flag_united_states_of_america
        "uy" //uruguay
        -> R.drawable.flag_uruguay
        "uz" //uzbekistan
        -> R.drawable.flag_uzbekistan
        "va" //holy see (vatican city state)
        -> R.drawable.flag_vatican_city
        "vc" //st vincent & the grenadines
        -> R.drawable.flag_saint_vicent_and_the_grenadines
        "ve" //venezuela, bolivarian republic of
        -> R.drawable.flag_venezuela
        "vg" //british virgin islands
        -> R.drawable.flag_british_virgin_islands
        "vi" //us virgin islands
        -> R.drawable.flag_us_virgin_islands
        "vn" //vietnam
        -> R.drawable.flag_vietnam
        "vu" //vanuatu
        -> R.drawable.flag_vanuatu
        "wf" //wallis and futuna
        -> R.drawable.flag_wallis_and_futuna
        "ws" //samoa
        -> R.drawable.flag_samoa
        "xk" //kosovo
        -> R.drawable.flag_kosovo
        "ye" //yemen
        -> R.drawable.flag_yemen
        "yt" //mayotte
        -> R.drawable.flag_martinique // no exact flag found
        "za" //south africa
        -> R.drawable.flag_south_africa
        "zm" //zambia
        -> R.drawable.flag_zambia
        "zw" //zimbabwe
        -> R.drawable.flag_zimbabwe
        else -> R.drawable.flag_transparent
    }

object CurrencyUtils {
    val TAG = CurrencyUtils.javaClass.simpleName
    fun getCurrency(countryCode: String): CurrencyBean? {
        val locale = Locale(Locale.getDefault().language, countryCode)
        try {
            Currency.getInstance(locale)?.let {
                return CurrencyBean(
                        countryName = locale.displayCountry,
                        currencyCode = it.currencyCode,
                        currencySymbol = it.symbol,
                        flagResId = countryCode.getFlagMasterResID
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in getting currency details for countryCode: ${countryCode}")
        }
        return null
    }
}