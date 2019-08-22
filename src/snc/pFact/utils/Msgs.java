package snc.pFact.utils;

/**
 * Msgs
 */
public enum Msgs {

    NOT_ENOUGH_PERMISSION("faction.not_enough_permission", "&4Yeterince yetkin yok!"),
    ENTER_AN_ARGUMENT("faction.enter_an_argument", "&cBir işlev gir!"),
    ENTER_A_PLAYER_NAME("faction.enter_a_player_name", "&cBir oyuncunun adını gir!"),
    FACTION_DISBANDED("faction.faction_disbanded", "&4Klanın dağıldı!"),
    KICKED_FROM_FACTION("faction.kicked_from_faction", "&4Klanından atıldın!"),
    COULDNT_FIND_FACTION("faction.couldnt_find_faction", "&4Klan bulunamadı: &r<faction>"),

    VALUE_ERROR("faction.value_error", "&4Geçerli bir değer gir!"),
    DONT_HAVE_A_CLAN("faction.dont_have_a_clan", "&4Bir klana mensup değilsin!"),
    ENTER_A_NAME("faction.enter_a_name", "&4Bir isim gir!"),
    COULDNT_FIND_PLAYER("faction.couldnt_find_player", "&cOyuncu bulunamadı:&r <player>"),
    ALREADY_HAS_FACTION("faction.already_has_faction", "&4Zaten bir klana mensupsun."),
    NOT_ENOUGH_MONEY("faction.not_enough_money", "&4Yeterince paran yok!"),
    NOT_A_MEMBER_OF_YOUR_FACTION("faction.not_a_member_of_your_faction", "&4Oyuncu klanının bir mensubu değil!"),

    NOT_A_MEMBER_OF_CLAN("faction.not_a_member_of_faction", "&4Oyuncu klanının bir mensubu değil!"),
    NOT_A_MEMBER_OF_CLAN_ANYMORE("faction.not_a_member_of_clan_anymore", "&4Artık bir klana mensup değilsin!"),

    HELP_KUR("faction.help.kur", "&b/klan kur <klan ismi>"), //
    HELP_BILGI("faction.help.bilgi", "&b/klan bilgi [klan]"),
    HELP_OYUNCU("faction.help.oyuncu", "&b/klan oyuncu <davet/at> <oyuncu>"), //
    HELP_KABULRET("faction.help.kabulret", "&b/klan <kabul/ret>"),
    HELP_YETKI("faction.help.yetki", "&b/klan yetki <ver/al> <oyuncu>"),
    HELP_KURUCUYAP("faction.help.kurucuyap", "&b/klan kurucuyap <oyuncu>"), //
    HELP_AYRIL("faction.help.ayril", "&b/klan ayrıl"), //
    HELP_EV("faction.help.ev", "&b/klan ev"), //
    HELP_EVYAP("faction.help.evyap", "&b/klan evyap"), //
    HELP_BAGIS("faction.help.bagis", "&b/klan bağış <miktar>"), //
    HELP_VIP("faction.help.vip", "&b/klan vip"),

    CHAT_FORMAT("chat_tags.format", " <tag>&6<faction> <rank>&3<player> >> &r<message>"),
    FACTION_OWNER_TAG("chat_tags.owner", "&7&lK. "), //
    FACTION_MODERATOR_TAG("chat_tags.moderator", "&7&lY. "), //
    SLACK_TAG("chat_tags.slacker", "&7&lAYLAK "), //
    VIP_TAG("chat_tags.vip", "&9&lVIP"), //
    TABLIST_SLACK_COLOR("tablist_tags.slack", "&r"), //
    TABLIST_MEMBER_COLOR("tablist_tags.member", "&3"), //
    TABLIST_VIP("tablist_tags.vip", "&9&lVIP"), //
    TABLIST_ADMIN("tablist_tags.admin", "&4ADMIN"), //

    KICKER_MESSAGE("faction.kicker_message", "&cOyuncu klanından atıldı: <player>"),
    CANT_LEAVE_FACTION_OWNER("faction.owner_cant_leave",
            "&4Kurucusu olduğun klandan ayrılamazsın!!%%" + "&cKlanını devretmek için:&r /klan kurucuyap <isim>"),
    PLAYER_IS_IN_CLAN_ALREADY("faction.player_is_in_clan_already", "&cOyuncu zaten senin klanında:&r <player>"),
    PLAYER_IS_IN_ANOTHER_CLAN("faction.player_is_in_another_clan", "&cOyuncu zaten bir klana üye:&r <player>"),
    PLAYER_IS_INVITED_BY_ANOTHER_CLAN("faction.player_is_invited_by_another_clan",
            "&cOyuncu başka bir klana davet edilmiş olabilir: <player>"),
    PLAYER_INVITED("faction.player_invited",
            "&2Oyuncu klanına davet edildi! Teklifini kabul ederse klanının bir üyesi olacak!: <player>"),
    INVITED_TO_CLAN("faction.invited_to_clan",
            "&2<faction> klanı tarafından davet edildin!%%&2Kabul etmek için: /klan kabul%%&2Reddetmek için: /klan ret"),
    NOT_FOUNDER_ANYMORE("faction.not_founder_anymore", "&4Artık klanının kurucusu değilsin!"),
    FOUNDER_NOW("faction.new_founder", "&bKlanının kurucusu artık sensin!"),
    FACTION_NAME_ERROR("faction.faction_name_error",
            "&4Girdiğin klan ismi uyumsuz!%%&4Klan isimleri yalnızca harf ve sayı içerebilir, özel karakterleri içeremez!%%&4Bu isimde başka bir klan oluşturulmuş olabilir."),
    FACTION_FOUNDED("faction.faction_founded", "&aKlanın başarıyla oluşturuldu::&r <faction>"),
    GIVEN_MAIN_CLAIM("faction.given_main_claim", "&aAna bölge bloğu envanterine eklendi!"),
    DONATED_TO_FACTION("faction.donated_to_faction", "&bKlanına yaptığın bağış miktarı: &a<amount>"),
    INFO("faction.info",
            "&7<faction>%%" + "&aKurucu: &r<founder>%%" + "&aYetkililer: &r<admins>%%" + "&aDiğer Üyeler: &r<players>%%"
                    + "&aSeviye: &r<level>%%" + "&aDeneyim: &r<xp>/<neededxp>%%" + "&aPrestij: &r<prestige>%%"
                    + "&aBanka: &r<money>"),
    INFO_OTHER("faction.info_other",
            "&7<faction>%%" + "&aKurucu: &r<founder>%%" + "&aYetkililer: &r<admins>%%" + "&aDiğer Üyeler: &r<players>%%"
                    + "&aSeviye: &r<level>"),
    INFO_OFFLINE_COLOR("faction.info_offline_color", "&7"), //
    INFO_ONLINE_COLOR("faction.info_online_color", "&a"),
    NO_INVITE("faction.no_invite", "&cBir davet almadın veya zaten bir klana mensupsun!"),
    JOINED_FACTION("faction.joined_faction", "&2<faction> &bklanına katıldın!"),
    JOINED_FACTION_BROADCAST("faction.joined_faction_broadcast", "&2<player> &bklana katıldı!"),
    REJECTED_INVITE("faction.rejected_invite", "&2<faction> &cklanının daveti reddedildi!"),
    GIVEN_ADMIN("faction.given_admin", "&2<player> &bartık klanında bir yetkili!"),
    TAKEN_ADMIN("faction.taken_admin", "&2<player> &bartık klanında bir yetkili değil!"),
    ADMIN_NOW("faction.admin_now", "&bArtık klanında bir yetkilisin!"),
    NOT_ADMIN_NOW("faction.not_admin_now", "&bArtık klanında bir yetkili değilsin!"),
    CHANGED_HOME("faction.changed_home", "&bKlan evi olduğun konuma eşitlendi!"),
    CANT_CHANGE_HOME("faction.cant_change_home", "&cKlan evi, klanın ana bölgesinin içerisinde olmak zorunda!"),
    NO_HOME("faction.no_home", "&4Klanının evi belirlenmemiş!"), //

    FACTION_LEVEL_UP_TITLE("faction.level_up_title", "&2Klanın seviye atladı"),
    FACTION_LEVEL_UP_SUBTITLE("faction.level_up_subtitle", "&aYeni klan seviyesi: &5<level>"),

    WARP_CANCELLED("faction.warp_cancelled", "&4Hareket ettiğin için ışınlanamadın!"),

    VIP_INFO("faction.vip_info", "&bVIP almak için adminlerden biriyle iletişime geç."),
    VIP_FOR("faction.vip_for", "&2<time> &bsüre vipsin."), //
    VIP_EXPIRED("faction.vip_expired", "&4Artık vip değilsin."),;

    public String id, sub;

    Msgs(String id, String sub) {
        this.id = id;
        this.sub = sub;
    }
}