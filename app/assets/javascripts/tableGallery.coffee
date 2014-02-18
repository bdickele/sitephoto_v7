jQuery ($) ->
    # ##############################################
    # LIST OF GALLERIES FOR TABLE
    # ##############################################
    $galleryTable = $('#galleryTable')
    galleryList = $galleryTable.data('list')
    $.get galleryList, (galleries) ->
        $.each galleries, (index, gallery) ->
            link = jsRoutes.controllers.gallery.Galleries.view(gallery.galleryId)['url']
            row =   "<tr>" +
                    "<td class=\"galleryThumbnailCol\">" +
                    "<a href=\"" + link + "\"><img src=\"" + gallery.thumbnail + "\" class='galleryThumbnail'></a></td>" +
                    "<td><h5><a href=\"" + link + "\">" + gallery.title + "</a></h5></td>" +
                    "</tr>"
            #alert(row)
            $galleryTable.append(row)