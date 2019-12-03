{
    const game_area_div = document.querySelector("#game-area");

    const title = document.createElement("h1");
    title.textContent = "title placeholder";

    const instructions = document.createElement("h3");
    instructions.textContent = "Click when the bars when they turn green!";

    const counter = document.createElement("h1");
    counter.textContent = "countdown";
    let timer = null;

    game_area_div.appendChild(title);
    game_area_div.appendChild(instructions);
    game_area_div.appendChild(counter);

    let score = 0;
    countdown(3);

    function countdown(value){
        counter.textContent = "Game will start in " + value;
        if(value < 1){
            clearTimeout(timer);
            // counter.textContent = "placeholder";
            counter.parentNode.removeChild(counter);
            loadGame();
        } else {
            value--;
            timer = setTimeout(() => countdown(value), 1000);
        }
    }

    function loadGame(){
        const game = document.createElement("div");
        game.setAttribute("id", "game");

        const ingredient_img = document.createElement("img");
        ingredient_img.setAttribute("id", "ingredient-image");
        const img = document.getElementsByClassName("game-in-progress")[0].firstChild.firstChild.src;
        ingredient_img.setAttribute("src", img);
        ingredient_img.setAttribute("draggable", false);

        const target1 = document.createElement("div");
        target1.setAttribute("id", "target1");
        target1.setAttribute("class", "target");

        const target2 = document.createElement("div");
        target2.setAttribute("id", "target2");
        target2.setAttribute("class", "target");

        const target3 = document.createElement("div");
        target3.setAttribute("id", "target3");
        target3.setAttribute("class", "target");

        const target4 = document.createElement("div");
        target4.setAttribute("id", "target4");
        target4.setAttribute("class", "target");

        const target5 = document.createElement("div");
        target5.setAttribute("id", "target5");
        target5.setAttribute("class", "target");

        game.appendChild(ingredient_img);
        game.appendChild(target1);
        game.appendChild(target2);
        game.appendChild(target3);
        game.appendChild(target4);
        game.appendChild(target5);

        game_area_div.appendChild(game);

        playGame();
    }

    function playGame(){
        const init_width = 350 * 0.4;
        const chopAt = [350 * 0.83, 350 * 0.67, 350 * 0.5, 350 * 0.33, 350 * 0.16];
        let target_cnt = 1;
        let targets = [$("#target1"), $("#target2"), $("#target3"), $("#target4"), $("#target5")]
        $("#target" + target_cnt).css("left", chopAt.pop());
        $("#target" + target_cnt).addClass("shrink_animation");

        for(let target of targets) {
            target.mousedown(function () {
                if ($("#target" + target_cnt).width() === 0) return;

                let targetwidth = parseFloat(window.getComputedStyle($("#target" + target_cnt)[0]).getPropertyValue("width"));
                if (targetwidth < init_width * 0.6) {
                    score++;
                } else {
                    $("#target" + target_cnt).addClass("fail");
                }
                $("#target" + target_cnt).css("-webkit-animation-play-state", "paused");
                // $("#target").removeClass("shrink_animation");

                setTimeout(function () {
                    if (chopAt.length) {
                        target_cnt++;
                        $("#target" + target_cnt).css("left", chopAt.pop());
                        $("#target" + target_cnt).addClass("shrink_animation");
                    } else {
                        endGame();
                    }
                }, 400);
            });

            target[0].addEventListener("animationend", function () {

                $("#target" + target_cnt).css("-webkit-animation-play-state", "paused");
                // $("#target").removeClass("shrink_animation");

                setTimeout(function () {
                    if (chopAt.length) {
                        target_cnt++;
                        $("#target" + target_cnt).css("left", chopAt.pop());
                        $("#target" + target_cnt).addClass("shrink_animation");
                    } else {
                        endGame();
                    }
                }, 400);
            }, false);
        }
    }

    function endGame(){
        const tab_instance = M.Tabs.getInstance(document.querySelectorAll(".tabs")[0]);
        tab_instance.select("items");

        while(game_area_div.firstChild){game_area_div.removeChild(game_area_div.firstChild);}

        console.log("you scored " + score);

        if(score > 3){
            M.toast({html: 'Good job!'});
            itemBoard.performAction();
        } else {
            const elem = document.getElementById("mistakes");
            let val = parseInt(elem.textContent) + 1;
            elem.textContent = val;
            M.toast({html: 'You can do better. Try again!'});
            itemBoard.failedAction();
        }

        for(let item of document.getElementsByClassName("game-in-progress")){
            item.classList.remove("game-in-progress");
        }

        current_game_css.parentNode.removeChild(current_game_css);
        current_game_script.parentNode.removeChild(current_game_script);
    }
}