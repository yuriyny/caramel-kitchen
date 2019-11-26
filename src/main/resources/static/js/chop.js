//-----------SUBROUTINE_B------------//
const subroutine_b = $("#subroutine_b");
const meter_blank = $("#meter_blank");
const target = $("#target");
const initWidth = 200;
let iters = 0;
const iterLimit = 5;

let targetVal = Math.floor(Math.random() * 501) + 50;
$("#target").css("left", targetVal);
$("#target").addClass("shrink_animation");

$("#target").mousedown(function(){
    if($("#target").width() === 0) return;

    iters++;
    let targetwidth = parseFloat(window.getComputedStyle($("#target")[0]).getPropertyValue("width"));
    let value = (1 - (targetwidth / initWidth)).toFixed(2);
    $("#target > p")[0].textContent = value;
    $("#target > p").show();
    // $("#target > p").css("animation-play-state", "running");
    $("#target > p").addClass("showScore_animation");
    $("#target").removeClass("shrink_animation");

    setTimeout(function() {
        if(iters < iterLimit){
            let targetVal = Math.floor(Math.random() * 501) + 50;
            $("#target").css("left", targetVal);
            // $("#target")[0].style.animation = '';
            $("#target").addClass("shrink_animation");
        }
        $("#target > p").css("display", "none");
    }, 1000);
})
$("#target")[0].addEventListener("animationend", function(){
    if($("#target")[0] !== event.target) return;  //if picks up the slider p animation, ignore this.

    iters++;
    $("#target > p")[0].textContent = "Failed";
    $("#target > p").show();
    // $("#target > p").css("animation-play-state", "running");
    $("#target > p").addClass("showScore_animation");
    $("#target").removeClass("shrink_animation");

    setTimeout(function() {
        if(iters < iterLimit){
            let targetVal = Math.floor(Math.random() * 501) + 50;
            $("#target").css("left", targetVal);
            $("#target").addClass("shrink_animation");
        }
        $("#target > p").css("display", "none");
    }, 1000);
}, false);