package com.gahyeonbot.config;

public class Description {
    // 명령어 설명 개요
    public static final String ALLHERE_NAME = "allhere";
    public static final String ALLHERE_DESC = "모든 보이스 채널의 사용자들을 현재 보이스 채널로 이동시킵니다.";
    public static final String ALLHERE_DETAIL = "디스코드 서버의 모든 사용자를 현재 보이스 채널로 집결시킵니다.";


    public static final String CLEAN_NAME = "clean";
    public static final String CLEAN_DESC = "채팅 삭제 명령어입니다. 전체 채팅과 내가 작성한 채팅 선택이 가능합니다.";
    public static final String CLEAN_DETAIL = "원하는 수만큼 메시지를 삭제합니다. 권장값은 100개 이하이며, 100개 초과 시 100개 단위로 삭제를 반복합니다.";

    public static final String INFO_NAME = "info";
    public static final String INFO_DESC = "명령어 사용법 안내서";
    public static final String INFO_DETAIL = "봇의 명령어 목록과 사용법을 안내합니다.";

    public static final String WITHOUT_NAME = "outwith";
    public static final String WITHOUT_DESC = "현재 보이스 채널에 있는 모든 사용자를 지정된 시간에 내보냅니다.";
    public static final String WITHOUT_DETAIL = """
        - **preset**: 미리 정의된 시간 옵션 (예: 1시간, 2시간).
        - **time**: 사용자가 직접 입력한 시간 (1~4자리 숫자 가능).
          - 4자리 숫자: 시간(2자리) + 분(2자리)
          - 3자리 숫자: 시간(1자리) + 분(2자리)
          - 1~2자리 숫자: 분(최대 99분)
        """;

    public static final String PLAY_NAME = "play";
    public static final String PLAY_DESC = "노래를 재생합니다.";
    public static final String PLAY_DETAIL = "검색할 노래 제목을 입력하세요.";

    public static final String QUEUE_NAME = "queue";
    public static final String QUEUE_DESC = "현재 대기열을 표시합니다.";
    public static final String QUEUE_DETAIL = "현재 대기열에 있는 모든 곡을 나열합니다.";

    public static final String BOTOUT_NAME = "removebots";
    public static final String BOTOUT_DESC = "현재 보이스 채널에서 모든 봇을 내보냅니다.";
    public static final String BOTOUT_DETAIL = "보이스 채널의 모든 봇을 내보내므로 필요한 봇이 있는지 확인하세요.";

    public static final String OUT_NAME = "soloout";
    public static final String OUT_DESC = "지정한 시간 후 보이스 채널 사용자를 내보냅니다.";
    public static final String OUT_DETAIL = """
        - **preset**: 미리 정의된 시간 옵션 (예: 1시간, 2시간).
        - **time**: 사용자가 직접 입력한 시간 (1~4자리 숫자 가능).
          - 4자리 숫자: 시간(2자리) + 분(2자리)
          - 3자리 숫자: 시간(1자리) + 분(2자리)
          - 1~2자리 숫자: 분(최대 99분)
        """;

    public static final String STOP_NAME = "stop";
    public static final String STOP_DESC = "현재 노래를 멈추고 대기열을 초기화합니다.";
    public static final String STOP_DETAIL = "현재 재생 중인 음악을 멈추고 대기열을 초기화합니다.";

    /*
    public static final String _NAME="";
    public static final String _DESC="";
    public static final String _DETAIL="";
    */




}
