//-----------SUBROUTINE_A------------//
const subroutine_a = $("#subroutine_a");
const meter = $("#meter_a");
const slider = $("#slider");
$("#slider").addClass("slide_animation");

$("#subroutine_a").click(function(){
    $("#slider").css("animation-play-state", "paused");
    let sliderPos = parseFloat(window.getComputedStyle($("#slider")[0]).getPropertyValue("left"));
    let barwidth = parseFloat(window.getComputedStyle($("#meter_a")[0]).getPropertyValue("width"));
    let value = (sliderPos/barwidth).toFixed(2);
    $("#slider > p")[0].textContent = value;

    $("#slider").addClass("hide");
    $("#slider > p").addClass("showScore_animation");
    setTimeout(function() {
        $("#slider > p").css("display", "none");
    }, 1000);
});
$("#slider")[0].addEventListener("animationend", function(){
    if($("#slider")[0] !== event.target) return;  //if picks up the slider p animation, ignore this.

    $("#slider > p")[0].textContent = "Failed";
    $("#slider").addClass("hide");
    $("#slider > p").addClass("showScore_animation");
    $("#subroutine_a").unbind();
    setTimeout(function() {
        $("#slider > p").css("display", "none");
    }, 1000);
}, false);