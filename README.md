# PlayCHAT!

https://shrouded-chamber-1869.herokuapp.com

TO PlayCHAT! é uma aplicação de chat simples que utiliza metodologias de Single Page Application com RestSerices. Neste projeto foi utilizado o Play Framework como tecnologia de back-end e AngularJS como front-end.

#### Single Page Application(SPA)
Single Page Application(SPA) são aplicações moduladas com apenas uma pagina, aonde o conteúdo é controlado por técnicas de AJAX fazendo com que as aplicações nao possuam 'reloads' em paginas HTML. A ideia do Single Page Application é de remover a responsabilidade do servidor em permutar paginas, diminuir a carga 'client-server' e trazer mais responsabilidade para o cliente, fazendo com que a performance e a experiencia do usuário melhore.

#### Rest Services
O Uso de serviços REST com JSON para a aplicação é um exemplo de como podemos simplificar o trafego de informações client-server. Serviços Resfull são webservices que não guardam estado(stateless) e que permitem consumir e produzir tipos variados de objetos, como por exemplo o JSON que são objetos javascripts simples de serem manipulados.

#### Hospedagem
Para publicação do projeto podemos usar uma solução de PaaS(CloudComputing) como Heroku, Amazon EC2, RedHat Openshift, entre outras soluções que podem variar de acordo com o tamanho do projeto e da forma de desenvolvimento. Ambas as soluções tem uma preocupação com escalabilidade a nível de servidores e hardware, o que deve sempre ser acompanhado de acordo com o trafego de usuários. Escolhi utilizar Heroku por ser uma solução com bastante "DevOps" (automatizada).

#### Estrutura do projeto
O projeto do chat foi desenvolvido utilizando Rest Services como comunicação entre client-server e Comet Sockets(Server-Send Events) como comunicação server-client. Na estrutura também foi ignorado qualquer arquivo 'view' do Play Framework e foi utilizados arquivos estáticos como solução final de client. Para fazer isso foi necessario mapear todos os arquivos estáticos do AngularJS dentro da pasta default 'public' do projeto.

As decisões mais importantes tomadas em projeto podem variar de caso a caso. Para este sistema de chat, a forma com que o servidor vai se conectar com o cliente e bastante importante para desenvolvermos uma boa performance no projeto. O Server-Send Events foi escolhido por ter bastante performatica, suporte do PlayFramework e por nao termos a necessidade de um WebSocket, ja que uma solucao unidirecional de server-client ja supre com as necessidades do projeto.

O Projeto e as tecnologias utilizadas tornam o desenvolvimento agil, devido a solução estática de view(compilação em tempo de execução, sem necessidade de permutações) e as facilidades de build automático que o PlayFramework fornece. Em contra partida o uso de APIs Third Party Authentication podem dificultar a execução de testes devido a problemas como cross domain, validação de domínios e etc...

#### Executando o projeto

Gere um clone do git na sua maquina. Com a ultima versão do PlayFramework instalado, entre no diretório do projeto e digite
```sh
$ activator
```
Dentro do activator CLI, execute 'run' para iniciar o servidor
```sh
$ run
```
