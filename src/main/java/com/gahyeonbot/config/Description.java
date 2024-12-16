package com.gahyeonbot.config;

public class Description {
    public static final String ALLHERE_NAME="allhere";
    public static final String ALLHERE_DESC="모든 보이스 채널의 사용자들을 현재 보이스 채널로 이동시킵니다.";
    public static final String ALLHERE_DETAIL="현디스코드서버의 모든 채널에있는 이용자를 사용자의 보이스채널로 집결시킵니다";

    public static final String CLEAN_NAME="clean";
    public static final String CLEAN_DESC="채팅 삭제 명령어입니다. 전체채팅과 내가작성한 채팅 선택이 가능합니다.";
    public static final String CLEAN_DETAIL="원하는 삭제 메시지 수대로 삭제합니다. 권장값은 100이하입니다. 100을 초과할시 100개단위씩 삭제를 반복합니다.";

    public static final String INFO_NAME="info";
    public static final String INFO_DESC="명령어 사용법 안내서";
    public static final String INFO_DETAIL="";

    public static final String OUTWITH_NAME="outwith";
    public static final String OUTWITH_DESC="현재 이 보이스채널에 입장하고 있는 모든 유저를 지정시간에 내보냅니다.";
    public static final String OUTWITH_DETAIL=
            "preset: 기본 제공하는 선택형 옵션입니다. 1시간,2시간과같은 기본 내보낼 시간설정을 통해 지정할수있습니다.\n" +
            "time: 사용자가 직접 시간을 지정해 세세한 내보내기 시간지정을 할수있습니다. 1~4자리 숫자를 입력가능하며 \n" +
            "숫자4개-> 시간(2자리) + 분(1자리) / 숫자 3개-> 시간(1자리)+분(2자리) / 숫자 1,2개 -> 분 으로 처리합니다";

    public static final String REMOVEBOTS_NAME="removebots";
    public static final String REMOVEBOTS_DESC="현재 보이스 채널에서 모든 봇을 내보냅니다.";
    public static final String REMOVEBOTS_DETAIL="보이스채널에 있는 모든 봇을 내보내기때문에 현재 필요한봇이 있는지 확인하시길 바랍니다";

    public static final String SOLOOUT_NAME="soloout";
    public static final String SOLOOUT_DESC="지정한 시간 후에 보이스채널에 있는 사용자를 내보냅니다.";
    public static final String SOLOOUT_DETAIL=
            "preset: 기본 제공하는 선택형 옵션입니다. 1시간,2시간과같은 기본 내보낼 시간설정을 통해 지정할수있습니다.\n" +
            "time: 사용자가 직접 시간을 지정해 세세한 내보내기 시간지정을 할수있습니다. 1~4자리 숫자를 입력가능하며 \n" +
            "숫자4개-> 시간(2자리) + 분(1자리) / 숫자 3개-> 시간(1자리)+분(2자리) / 숫자 1,2개 -> 분 으로 처리합니다";

    /*
    public static final String _NAME="";
    public static final String _DESC="";
    public static final String _DETAIL="";
    */




}
