jQuery ($) ->
    # ##############################################
    # LIST OF GALLERIES FOR COMBO
    # ##############################################
    $galleryMenu = $('#galleryMenu')
    galleryList = $galleryMenu.data('list')
    $.get galleryList, (galleries) ->
        $.each galleries, (index, gallery) ->
            row = "<li><a href='" + jsRoutes.controllers.gallery.Galleries.view(gallery.id)['url'] + "'>" + gallery.title + "</a></li>"
            #alert(row)
            $galleryMenu.append(row)