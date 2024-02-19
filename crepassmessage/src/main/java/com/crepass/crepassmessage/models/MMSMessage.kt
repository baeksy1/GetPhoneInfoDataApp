package com.crepass.crepassmessage.models

class MMSMessage {

    /**CHARSET 텍스트 파트의 문자 집합입니다.*/
    var chset:String?=null

    /**CONTENT_DISPOSITION 컨텐츠의 배치 방식을 설명합니다.*/
    var cd:String?=null

    /**CONTENT_ID 컨텐츠의 ID입니다.*/
    var cid:Int?=null

    /**CONTENT_LOCATION 컨텐츠의 위치입니다.*/
    var cl:Int?=null

    /**CONTENT_TYPE 파트의 컨텐츠 유형입니다 (예: image/jpeg, text/plain 등).*/
    var ct:String?=null

    /**CT_START 컨텐츠 유형의 시작 부분입니다.*/
    var ctt_s:Int?=null

    /**CT_TYPE 컨텐츠 유형입니다.*/
    var ctt_t:String?=null

    /**FILENAME 파일의 이름입니다.*/
    var fn:String?=null

    /**MSG_ID 메시지의 ID입니다. 이는 파트가 속한 MMS 메시지를 식별하는 데 사용됩니다.*/
    var mid:Int?=null

    /**NAME 파트의 이름입니다.*/
    var name:String?=null

    /**SEQ 파트의 순서입니다.*/
    var seq:Int?=null

    /**TEXT 메시지 텍스트입니다.*/
    var text : ArrayList<String>? = null

    /**_DATA 파트의 바이너리 데이터가 파일 시스템에 저장된 위치입니다.*/
    var _data:String?=null

    /** ADDRESS 보낸사람 번호*/
    var address:String ? =null

}