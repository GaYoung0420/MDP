checkedId = new Array();

$(document).ready(function () {
    // emotion Swiper 객체 생성
    emotionSwiper = new Swiper(".emotionSwiper", {
        // Swiper 속성 설정
        autoHeight: true,
        // swipe 터치 막음
        allowTouchMove: false,
        // 키보드 이벤트 막기
        keyboard: {
            enabled: false,
            onlyInViewport: false,
            pageUpDown: false
        },
    });
    // 감정 Swiper, 선택 박스, 버튼 hide
    $(".emotionSwiper").hide();
    $(".step6_container_select").hide();
    $("#step6_next").hide();

    // 감정카드 카테고리 선택시 Swiper 움직임

    $("input:radio[name=step6_emotion_category]").change(function () {
        $("#emotion_common").removeClass("animate_common");
        $("#emotion_thanks").removeClass("animate_thanks");
        $("#emotion_sorry").removeClass("animate_sorry");
        $("#emotion_celebration").removeClass("animate_celebration");
        $("#emotion_love").removeClass("animate_love");
        $("#emotion_say").removeClass("animate_say");

        $(".emotionSwiper").fadeIn(600);

        let emotion_radio = $('input:radio[name="step6_emotion_category"]:checked').val();

        switch (emotion_radio) {
            case "1": {
                emotionSwiper.slideTo(0);
                break;
            }
            case "2": {
                emotionSwiper.slideTo(1);
                break;
            }
            case "3": {
                emotionSwiper.slideTo(2);
                break;
            }
            case "4": {
                emotionSwiper.slideTo(3);
                break;
            }
            case "5": {
                emotionSwiper.slideTo(4);
                break;
            }
            case "6": {
                emotionSwiper.slideTo(5);
                break;
            }
        }
    });

    $(".emotion_category").click(function () {
        $("#emotion_1").removeClass("animate_common");
        $("#emotion_2").removeClass("animate_thanks");
        $("#emotion_3").removeClass("animate_sorry");
        $("#emotion_4").removeClass("animate_celebration");
        $("#emotion_5").removeClass("animate_love");
        $("#emotion_6").removeClass("animate_say");

        $(".emotionSwiper").fadeIn(600);
    });

    for (let i = 1; i < 7; i++) {
        clickCategory(i);
    }

});

$('.emotion_card_text').on('click', function () {
    let idx = $(this).prop("id");
    let isChecked = $(this).prop("check");
    $("html").animate({ scrollTop: 150 }, 400);

    $(".step6_container_select").fadeIn(600);
    $("#step6_next").delay(300).fadeIn(600);

    // 감정단어 삭제
    if (isChecked) {
        deleteEmotion(this);
        $(this).removeClass("emotion_card_text_checked");
        $(this).prop("check", false);
    }
    // 감정단어 추가
    else {
        addEmotion($(this).clone());
        $(this).addClass("emotion_card_text_checked");
        $(this).prop("check", true);
    }
});

// 내가 선택한 감정단어 : 추가
function addEmotion(emotion) {
    let last_node_count = $("#select_word").children().last().children().length;
    checkedId.push(emotion);
    $(".select_word").append(emotion)

    // 내가 선택한 감정단어 클릭시 지우기
    $(emotion).click(function () {
        deleteEmotion(emotion);
        let smaeID = $(emotion).prop("id");
        $("#" + smaeID).prop("check", false);
        $("#" + smaeID).removeClass("emotion_card_text_checked");
    });

}
// 내가 선택한 감정단어 : 삭제
function deleteEmotion(emotion) {
    $(".select_word").children().each(function (index, item) {
        if ($(emotion).prop("id") == $(item).prop("id")) {
            $(item).remove();
        }
    });
}

// 감정 카테고리 클릭
function clickCategory(idName) {
    $("#emotion_" + idName).click(function () {
        emotion_radio = $("#emotion_" + idName).attr("value");

        switch (emotion_radio) {
            case "1": {
                emotionSwiper.slideTo(0);
                removeClass(1);
                break;
            }
            case "2": {
                emotionSwiper.slideTo(1);
                removeClass(2);
                break;
            }
            case "3": {
                emotionSwiper.slideTo(2);
                removeClass(3);
                break;
            }
            case "4": {
                emotionSwiper.slideTo(3);
                removeClass(4);
                break;
            }
            case "5": {
                emotionSwiper.slideTo(4);
                removeClass(5);
                break;
            }
            case "6": {
                emotionSwiper.slideTo(5);
                removeClass(6);
                break;
            }
        }
    });

    function removeClass(num) {

        for (let i = 1; i <= 6; i++) {

            if (i == num) {
                $("#emotion_" + num).addClass("emotion_checked");
                $("#emotion_text_" + num).addClass("emotion_checked_text");
            }
            else {
                $("#emotion_" + i).removeClass("emotion_checked");
                $("#emotion_text_" + i).removeClass("emotion_checked_text");
            }
        }
    }
}