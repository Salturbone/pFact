package snc.pFact.utils;

/**
 * Msgs
 */
public enum Msgs {

    NOT_ENOUGH_PERMISSION("faction.not_enough_permission", "&4Yeterince yetkin yok!"),
    CHANGED_HOME("faction.changed_home", "&bKlan evi olduğun konuma eşitlendi!"),
    NOT_FOUNDER_ANYMORE("faction.not_founder_anymore", "&4Artık klanının kurucusu değilsin!"),
    NEW_FOUNDER("faction.new_founder", "&bKlanının kurucusu artık sensin!"),
    FACTION_DISBANDED("faction.faction_disbanded", "&4Klanın dağıldı!"),
    VALUE_ERROR("faction.value_error", "&4Geçerli bir değer gir!"),
    DONT_HAVE_A_CLAN("faction.dont_have_a_clan", "&4Bir klana mensup değilsin!"),
    ENTER_A_NAME("faction.enter_a_name", "&4Bir isim gir!"),
    FACTION_NAME_ERROR("faction.faction_name_error", "&4Girdiğin klan ismi uyumsuz!"),
    FACTION_NAME_ERROR2("faction.faction_name_error2",
            "&4Klan isimleri yalnızca harf ve sayı içerebilir, özel karakterleri içeremez!"),
    FACTION_NAME_ERROR3("faction.faction_name_error3", "&4Bu isimde başka bir klan oluşturulmuş olabilir."),
    FACTION_FOUNDED("faction.faction_founded", "&bKlanın başarıyla oluşturuldu: <faction>"),
    GIVEN_MAIN_CLAIM("faction.given_main_claim", "&bAna bölge bloğu envanterine eklendi!"),
    ALREADY_HAS_FACTION("faction.already_has_faction", "&4Zaten bir klana mensupsun."),
    DONATED_TO_FACTION("faction.donated_to_faction", "&bKlanına yaptığın bağış miktarı: &a<amount>"),
    NOT_ENOUGH_MONEY("faction.not_enough_money", "&4Yeterince paran yok!"),
    INFO_FACTION("faction.info.faction", "&7<faction>"), INFO_FOUNDER("faction.info.founder", "&bKurucu: &2<player>"),
    INFO_ADMINS("faction.info.admins", "&bYetkililer: &2<players>"),
    INFO_PLAYERS("faction.info.players", "&bDiğer Üyeler: &2<players>"),
    INFO_LEVEL("faction.info.level", "&bSeviye: &2<amount>"), INFO_EXP("faction.info.exp", "&bDeneyim: &2<amount>"),
    INFO_PRESTIGE("faction.info.prestige", "&bPrestij: &2<amount>"),
    INFO_BANK("faction.info.bank", "&bBanka: &2<amount>"),
    NOT_A_MEMBER_OF_CLAN("faction.not_a_member_of_faction", "&4Oyuncu klanının bir mensubu değil!"),
    NOT_A_MEMBER_OF_CLAN_ANYMORE("faction.not_a_member_of_clan_anymore", "&4Artık bir klana mensup değilsin!"),

    HELP_KURUCUYAP("faction.help.kurucuyap", "/klan kurucuyap <oyuncu>"),
    HELP_KUR("faction.help.kur", "/klan kur <klan ismi>"), HELP_BILGI("faction.help.bilgi", "/klan bilgi"),
    HELP_OYUNCU("faction.help.oyuncu", "/klan oyuncu <davet/at> <oyuncu>"),
    HELP_YETKI("faction.help.yetki", "/klan yetki <ver/al> <oyuncu>"),
    HELP_KABULRET("faction.help.kabulret", "/klan <kabul/ret>"),;

    public String id, sub;

    Msgs(String id, String sub) {
        this.id = id;
        this.sub = sub;
    }
}