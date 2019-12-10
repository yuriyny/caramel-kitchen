{
    const return_tab = document.getElementById("return-tab");
    return_tab.innerHTML = "Exit game";
    return_tab.addEventListener("mouseup", exitGame);

    const game_area_div = document.querySelector("#game-area");

    const title = document.createElement("h1");
    title.setAttribute("style", "text-transform: capitalize");
    title.textContent = itemBoard.relevent_action + " " + itemBoard.getNameByID(itemBoard.relevent_id);

    const instructions = document.createElement("h3");
    instructions.textContent = "Click the bars before you peel too much of the edges!";

    const counter = document.createElement("h1");
    counter.textContent = "countdown";
    let timer = null;

    const game_elements = document.createElement("div");
    game_elements.setAttribute("id", "game_elements");

    game_area_div.appendChild(title);
    game_area_div.appendChild(instructions);
    game_area_div.appendChild(counter);
    game_area_div.appendChild(game_elements);

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

        const edge1 = document.createElement("div");
        edge1.setAttribute("id", "edge1");
        edge1.setAttribute("class", "target");

        const edge2 = document.createElement("div");
        edge2.setAttribute("id", "edge2");
        edge2.setAttribute("class", "target");

        const edge3 = document.createElement("div");
        edge3.setAttribute("id", "edge3");
        edge3.setAttribute("class", "target");

        const edge4 = document.createElement("div");
        edge4.setAttribute("id", "edge4");
        edge4.setAttribute("class", "target");

        game.appendChild(ingredient_img);
        game.appendChild(edge1);
        game.appendChild(edge2);
        game.appendChild(edge3);
        game.appendChild(edge4);

        game_elements.appendChild(game);

        score = 0;
        playGame();
    }

    function playGame(){
        $("#edge1").addClass("extend_horizontal_animation");
        const max_length = 320;
        let edge_cnt = 1;
        let edges = [$("#edge1"), $("#edge2"), $("#edge3"), $("#edge4")];

        for(let edge of edges){
            edge.mousedown(function(){
                let check = 0;
                if(edge_cnt % 2 === 1){
                    check = parseFloat(window.getComputedStyle($("#edge" + edge_cnt)[0]).getPropertyValue("width"));
                } else {
                    check = parseFloat(window.getComputedStyle($("#edge" + edge_cnt)[0]).getPropertyValue("height"));
                }
                if (check > max_length * 0.7) {
                    score++;
                }
                $("#edge" + edge_cnt).css("-webkit-animation-play-state", "paused");
                edge.unbind();

                setTimeout(function () {
                    if (edge_cnt < 4) {
                        edge_cnt++;
                        if(edge_cnt % 2 === 1){
                            $("#edge" + edge_cnt).addClass("extend_horizontal_animation");
                        } else {
                            $("#edge" + edge_cnt).addClass("extend_vertical_animation");
                        }
                    } else {
                        endGame();
                    }
                }, 400);
            });

            edge[0].addEventListener("animationend", function () {
                $("#edge" + edge_cnt).css("-webkit-animation-play-state", "paused");
                edge.unbind();

                setTimeout(function () {
                    if (edge_cnt < 4) {
                        edge_cnt++;
                        if(edge_cnt % 2 === 1){
                            $("#edge" + edge_cnt).addClass("extend_horizontal_animation");
                        } else {
                            $("#edge" + edge_cnt).addClass("extend_vertical_animation");
                        }
                    } else {
                        endGame();
                    }
                }, 400);
            }, false);
        }
    }

    function endGame(){
        if(score > 2){
            M.toast({html: 'Good job!'});
            itemBoard.performAction();
            itemBoard.updateMenu();
            exitGame();
        } else {
            const elem = document.getElementById("mistakes");
            let val = parseInt(elem.textContent) + 1;
            elem.textContent = val;
            const counter = document.getElementById("mistake-counter");
            counter.classList.remove("mistake-notice");
            void counter.offsetWidth;
            counter.classList.add("mistake-notice");
            M.toast({html: 'You can do better. Try again!'});
            resetGame();
        }
    }

    function resetGame(){
        while(game_elements.firstChild){ game_elements.removeChild(game_elements.firstChild); }
        loadGame();
    }

    function customClean(){
        clearTimeout(timer);
    }

    function exitGame(){
        customClean();

        const tab_instance = M.Tabs.getInstance(document.querySelectorAll(".tabs")[0]);
        tab_instance.select("items");

        while(game_area_div.firstChild){game_area_div.removeChild(game_area_div.firstChild);}

        console.log("you scored " + score);

        for(let item of document.getElementsByClassName("game-in-progress")){
            item.classList.remove("game-in-progress");
        }

        itemBoard.clearAction();
        return_tab.innerHTML = "Your Items";
        return_tab.removeEventListener("mouseup", exitGame);

        current_game_css.parentNode.removeChild(current_game_css);
        current_game_script.parentNode.removeChild(current_game_script);
    }
}