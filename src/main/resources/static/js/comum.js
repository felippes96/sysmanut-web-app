// Validação genérica: todo <form data-validar> é checado antes do envio.
const EMAIL=/^[^@\s]+@[^@\s]+\.[^@\s]+$/;
function mensagem(c){
  const v=c.value.trim();
  if(c.required&&!v)return"Campo obrigatório.";
  if(v&&c.type==="email"&&!EMAIL.test(v))return"E-mail inválido.";
  if(v&&c.minLength>0&&v.length<c.minLength)return"Mínimo de "+c.minLength+" caracteres.";
  if(v&&c.name==="telefone"&&v.replace(/\D/g,"").length<10)return"Telefone incompleto.";
  return"";
}
function validar(c){
  const m=mensagem(c);c.classList.toggle("invalido",!!m);
  const e=c.nextElementSibling;if(e&&e.classList.contains("erro"))e.textContent=m;return!m;
}
function mascaraTelefone(t){
  let d=t.value.replace(/\D/g,"").slice(0,11);
  d=d.replace(/^(\d{2})(\d)/,"($1) $2").replace(/(\d{4,5})(\d{4})$/,"$1-$2");t.value=d;
}
document.querySelectorAll("form[data-validar]").forEach(f=>{
  f.noValidate=true;
  f.querySelectorAll("input[name=telefone]").forEach(t=>t.addEventListener("input",()=>mascaraTelefone(t)));
  f.querySelectorAll("input").forEach(c=>c.addEventListener("blur",()=>validar(c)));
  f.addEventListener("submit",ev=>{
    ev.preventDefault();
    const ok=[...f.querySelectorAll("input[required],input[type=email]")].map(validar).every(Boolean);
    if(ok){f.hasAttribute("data-servidor")?f.submit():f.dispatchEvent(new CustomEvent("valido"));}
  });
});
