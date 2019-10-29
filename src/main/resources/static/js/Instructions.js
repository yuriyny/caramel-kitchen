/**
 * The DOM element[s] for the recipe instructions.
 */
class Instructions{
    constructor(instructions_ul, instruction_list){
        this.instructions_ul = instructions_ul;
        this.instruction_list = instruction_list;
        if(document.getElementById("comment_enter")){
            let cmt_input = document.getElementById("comment_input");
            let cmt_btn = document.getElementById("comment_enter");
            cmt_btn.onclick = () => {
                this.addLabComment(cmt_input.value)
                cmt_input.value = "";
            };
            cmt_input.onkeypress = e => {
                if(e.keyCode === 13){
                    this.addLabComment(cmt_input.value)
                    cmt_input.value = "";
                }
            }
        }
    }

    addLabStep(text){
        const li = document.createElement("li");
        const step = document.createElement("div");
        const del = document.createElement("p");
        del.textContent = "\u00D7";
        del.onclick = () => { this.deleteLabStep(li); }
        step.setAttribute("class", "lab_step");
        step.textContent = text;
        step.appendChild(del);
        li.appendChild(step);
        this.instructions_ul.appendChild(li);
    }

    addLabComment(text){
        if(!text) return;

        const li = document.createElement("li");
        const step = document.createElement("div");
        const del = document.createElement("p");
        del.textContent = "\u00D7";
        del.onclick = () => { this.deleteLabStep(li); }
        step.setAttribute("class", "lab_step comment");
        step.textContent = text;
        step.appendChild(del);
        li.appendChild(step);
        this.instructions_ul.appendChild(li);
    }

    deleteLabStep(li){
        li.parentNode.removeChild(li);
    }
}