/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
BI = {
    defaultSuccess: function (rpta) {
        console.log(rpta.apiEstado === 'ok');
        if (rpta.apiEstado === 'ok') {
            Swal.fire(
                'Ok',
                rpta.apiMensaje,
                'success'
            );
        } else if (rpta.apiEstado === 'error') {
            Swal.fire(
                "Error",
                rpta.apiMensaje,
                "error"
            );
        }
    },
    defaultError: function (err) {
        Swal.fire({
            title: "Error",
            text: "Comun\u00edquese con Soporte T\u00e9cnico",
            icon: "error"
        });
        //$("#divLoading").show();
    },
    defaultTable: function (id, error) {
        try {
            $('#' + id + ' tbody').empty().append('<tr><td colspan="' + $('#' + id + ' th').length + '" class="text-center">' + error.apiMensaje + '</td></tr>');
        } catch (err) {
            console.log(err);
        }
    },
    ordenar: function (ev, ordenamiento, controller) {
        ev.preventDefault();
        controller.listParams.paginacion.pagina = 1;
        var me = this;

        $('thead span').empty('');
        var newOrder = ev.currentTarget.attributes["data-order"].value;

        try {
            if (ordenamiento === newOrder + ' asc') {
                ordenamiento = newOrder + ' desc';
                ev.currentTarget.firstElementChild.innerHTML = "&#8595;";//↓
            } else {
                ordenamiento = newOrder + ' asc';
                ev.currentTarget.firstElementChild.innerHTML = "&#8593;";//↑
            }

            controller.listParams.ordenamiento = ordenamiento;
        } catch (err) {
            BI.logError(err);
        }

        $('.paginate_button.page-item.first a').click();

        return ordenamiento;
    },
    mostrarPagina: function (ev,controller) {
        var me = controller;
        var tipoPagina = ev.currentTarget.id;

        if (tipoPagina === me.listParams.primero) {
            me.listParams.paginacion.pagina = 1;
        } else if (tipoPagina === me.listParams.anterior) {
            if (me.listParams.paginacion.pagina !== 1) {
                me.listParams.paginacion.pagina = me.listParams.paginacion.pagina - 1;
            }
        } else if (tipoPagina === me.listParams.siguiente) {
            if (me.listParams.paginacion.pagina !== me.listParams.paginacion.totalPaginas) {
                me.listParams.paginacion.pagina = me.listParams.paginacion.pagina + 1;
            }
        } else if (tipoPagina === me.listParams.ultimo) {
            me.listParams.paginacion.pagina = me.listParams.paginacion.totalPaginas;
        }
    },
    exportarExcel: function (nameFile, url) {
        
        if ($('#tablaResultado tr').length === 0) {
            Swal.fire({
                title: "Error",
                text: "Realice una búsqueda.",
                icon: "error"
            });
        }
        else {
            //$("#divLoading").show();
            $.ajax({
                url: url,
                method: 'GET',
                xhrFields: {
                    responseType: 'blob'
                },
                success: function (data) {
                    console.log(data);
                    var a = document.createElement('a');
                    var url = window.URL.createObjectURL(data);
                    a.href = url;
                    a.download = nameFile + '.xls';
                    a.click();
                    window.URL.revokeObjectURL(url);
                    $("#divLoading").css('display', 'none');
                }
            });
        }
    },
    MostrarPopupError: function(mensaje = ''){
        var arreglo = Array();
        arreglo = mensaje.split(".");
        let texto = "";
        arreglo.forEach(function (elemento) {
            if (elemento.length !== 0) {
                texto += '<li class="text-left">' + elemento.replace('_', '.') + '.</li>';
            }
        });
        const wrapper = document.createElement('ul');
        wrapper.innerHTML = texto;
        Swal.fire({
            title: 'Error',
            icon: "error",
            html: wrapper
        });
    },
    ConfirmaEliminar: function (ent) {
        return "\u00bfEst\u00e1 seguro de eliminar " + ent + "?";
    },
    convertFecha: function(fecha, tipo){
        var sep1 = '/', sep2 = '-', nuevaFecha;
        if (fecha) {
            if (tipo) {
                sep1 = '-';
                sep2 = '/';
            }
            nuevaFecha = fecha.split(sep1);
            return nuevaFecha[2] + sep2 + nuevaFecha[1] + sep2 + nuevaFecha[0];
        }
    },
    tableToExcel: function(table, name){
        var uri = 'data:application/vnd.ms-excel;base64,'
        , template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--></head><body><table>{table}</table></body></html>'
        , base64 = function(s) { return window.btoa(unescape(encodeURIComponent(s))); }
        , format = function(s, c) { return s.replace(/{(\w+)}/g, function(m, p) { return c[p]; }); }
      
        var ctx = {worksheet: name || 'Worksheet', table: table};
        window.location.href = uri + base64(format(template, ctx));
    } 
};

