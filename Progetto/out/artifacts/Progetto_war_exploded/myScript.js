
var app = new Vue ({
    el: '#app',
    data: {
        account : '',
        pass : '',
        link : '/Progetto/ServletController',

        mostrareLog : true,
        admin : false,
        celdas : [],
        elencoPre : [],
        mostrareElenPre : false,
        erroreConn : false,
        erroreDocNome : false,
        erroreDocCogome : false,
        nome : '',
        cognome : '',
        corso : '',
        erroreCorso : false,
        elencoDoc : [],
        elencoCorsi : [],
        docSelect: '',
        corSelect: '',
        associzioni: [],
        ore: [15,16,17,18],
        giorni: ["LUNEDI","MARTEDI","MERCOLEDI","GIOVEDI","VENERDI"]

    },
    computed:{
        attivi: function() {
            return this.elencoPre.filter(function(item) {
                return item.stato == "ATTIVA";
            })
        },
        effettuati: function() {
            return this.elencoPre.filter(function(item) {
                return item.stato == "EFFETTUATA";
            })
        },
        disdeti: function() {
            return this.elencoPre.filter(function(item) {
                return item.stato == "CANCELLATA";
            })
        }
    },
    methods:{
        inCelda: function (g,o) {
            return this.celdas.filter(function (item) {
                return item.giorno === g && item.ora === o;
            })
        },
        getCeldas:function() {
            var self = this;
            $.post(this.link, {azione: "oraLibera", utente: this.account}, function (data) {
                self.celdas = data;
            });
            console.log("hola" + self.celdas.length);
        },
        getDocenti:function() {
            var self = this;
            $.post(this.link, {azione: "elencoDoc", utente: this.account}, function (data) {
                self.elencoDoc = data;
            });
        },
        getPrenotazioni:function() {
            var self = this;
            $.post(this.link, {azione: "elencoPre", utente: this.account}, function (data) {
                self.elencoPre = data;
            });
        },
        getCorsi:function() {
            var self = this;
            $.post(this.link, {azione: "elencoCor", utente: this.account}, function (data) {
                self.elencoCorsi = data;
            });
        },
        getAssoc:function() {
            var self = this;
            $.post(this.link, {azione: "elencoAsso", utente: this.account}, function (data) {
                self.associzioni = data;
            });
        },
        nuovoDoc:function(){
            var self = this;
            self.erroreDocNome = (this.nome == "")
            self.erroreDocCognome = (this.cognome == "");
            if (!self.erroreDocNome && !self.erroreDocCognome) {
                $.post(this.link, {azione: "nuovoDoc", nome: this.nome, cognome: this.cognome}, function (data) {
                    console.log(self.elencoDoc.includes(data));
                    console.log(data);
                    console.log(self.elencoDoc);
                    if (!(self.elencoDoc.includes(data))) {
                        alert(self.nome + " " + self.cognome + " ha stato inserito.");
                        self.elencoDoc.push(JSON.parse(data));
                    }
                    else{
                        alert(self.nome + " " + self.cognome +  " è già registrato.");
                    }
                    self.nome = '';
                    self.cognome = '';
                });
            }

        },
        rimDoc:function(d){
            var self = this;
            $.post(this.link, {azione: "rimDoc", docente: JSON.stringify(d)});
            alert( d.nome  + " "+ d.cognome +" ha stato rimosso.");
            self.elencoDoc = self.elencoDoc.filter( el => el !== d );
            self.getAssoc();


        },

        nuovoCor:function(){
            var self = this;
            self.erroreCorso = (this.corso == "");
            if (!self.erroreCorso) {
                $.post(this.link, {azione: "nuovoCor", corso: this.corso}, function (data) {
                    if (!(self.elencoCorsi.includes(data))) {
                        alert("Il corso " + self.corso + " ha stato inserito.");
                        self.elencoCorsi.push(JSON.parse(data));
                    }
                    else{
                        alert("Il corso " + self.corso + " è già registrato.");
                    }
                    self.corso = '';
                });
            }
        },

        rimCorso:function(c){
            var self = this;
            $.post(this.link, {azione: "rimCorso", corso: JSON.stringify(c)} , function(data) {

            });
            alert( c.titulo +" ha stato rimosso.");
            self.elencoCorsi = self.elencoCorsi.filter( el => el !== c );
            self.getAssoc();

        },

        nuovaAssoc:function(d,c){
            var self = this;
            self.erroreCorso = (c.titulo == "");
            self.erroreDocNome = (d.nome == "");
            self.erroreDocCognome = (d.cognome == "");
            if (!self.erroreCorso && !self.erroreDocNome && !self.erroreDocCognome ) {
                $.post(this.link, {azione: "nuovaAssoc", corso: JSON.stringify(c), docente: JSON.stringify(d)} , function(data) {
                    alert("Associazione creata.");
                    // inserto il nuovo elemento nell'elenco senza chiamare al servLet una volta ancora
                    var trovato = false;
                    for (var i =0; i < self.associzioni.length; i++){
                        if ((self.associzioni)[i]["docente"]["nome"] === d.nome && (self.associzioni)[i]["docente"]["cognome"] === d.cognome) {
                            ((self.associzioni)[i]["corso"]).push(c);
                            trovato = true;
                            break;
                        }
                    }
                    if (!trovato)
                        self.associzioni.push(JSON.parse(data));
                });

            }
        },

        rimAssoc:function(d,c){
            var self = this;
            $.post(this.link, {azione: "rimAsso", corso: JSON.stringify(c), docente: JSON.stringify(d)} , function(data) {

            });
            self.getAssoc();
            alert("Associazione rimossa.");
        },

        effettuare:function(p){
            var self = this;
            $.post(this.link, {azione: "effettuare", pre: JSON.stringify(p)});
            p.stato = "EFFETTUATA";
        },
        disdire:function(p){
            var self = this;
            $.post(this.link, {azione: "disdire", pre: JSON.stringify(p)});
            p.stato = "CANCELLATA";
        },
        enviare:function(){
            var self = this;
            $.post(this.link, {azione: "connessione", utente: this.account , password: this.pass} , function(data) {
                switch(data){
                    case "a":
                        self.admin = false;
                        self.erroreConn = false;
                        self.mostrareLog = false;
                        self.getPrenotazioni();
                        self.getCeldas();
                        self.mostrareElenPre = true;
                        break;
                    case "ad":
                        self.getDocenti();
                        self.getCorsi();
                        self.getPrenotazioni();
                        self.getAssoc();
                        self.getCeldas();
                        self.admin = true;
                        self.mostrareLog = false;
                        self.erroreConn = false;
                        self.mostrareElenPre = true;
                        break;

                    case "f":
                        self.erroreConn = true;
                        break;


                }

            });
        }




    }
});
