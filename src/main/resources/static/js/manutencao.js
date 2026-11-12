// Mesmas regras da CalculadoraManutencao (Java): total = (mão de obra + peças) - desconto%, 2 casas, HALF_UP.
const num=s=>parseFloat(String(s).replace(",","."))||0;
const arred=v=>Math.round((v+Number.EPSILON)*100)/100;
const brl=v=>v.toLocaleString("pt-BR",{style:"currency",currency:"BRL"});
const pecas=document.querySelector("#pecas"),erro=document.querySelector("#erro-desconto");
function calcular(){
  const desc=num(document.querySelector("#desconto").value);
  const sub=num(document.querySelector("#maoDeObra").value)+[...pecas.querySelectorAll("input")].reduce((s,i)=>s+num(i.value),0);
  const invalido=desc<0||desc>100;erro.textContent=invalido?"O desconto deve estar entre 0% e 100%.":"";
  document.querySelector("#total").textContent=invalido?"—":brl(arred(sub-arred(sub*desc/100)));
  const d=new Date(document.querySelector("#data").value+"T00:00");
  document.querySelector("#garantia").textContent=isNaN(d)?"—":new Date(d.getTime()+90*864e5).toLocaleDateString("pt-BR");
}
document.querySelector("#addPeca").addEventListener("click",()=>{
  const l=document.createElement("div");l.className="linha";
  l.innerHTML='<input type="text" name="pecas" inputmode="decimal" placeholder="Valor da peça (R$)"><button type="button" class="btn sec">Remover</button>';
  l.querySelector("button").onclick=()=>{l.remove();calcular();};pecas.append(l);
});
document.querySelector("form").addEventListener("input",calcular);calcular();
