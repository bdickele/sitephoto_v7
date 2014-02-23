jQuery ($) ->
    $menu = $('#mainMenu')
    menuJSon = $menu.data('list')
    #alert(menuJSon)
    $.get menuJSon, (elements) ->
            $.each elements, (index, element) ->
                children = element.children
                if children isnt null
                    htmlCode = "<li><a href=\"#\">" + element.label + "</a>"
                    htmlCode += "<ul class=\"dl-submenu\">"

                    $.each children, (index2, child) ->
                        galleryTitle = child.label
                        galleryId = child.id
                        link = jsRoutes.controllers.Galleries.view(galleryId)['url']
                        htmlCode += "<li><a href=\"" + link + "\">" + galleryTitle + "</a></li>"

                    htmlCode += "</ul>"
                    htmlCode += "</li>"

                    #alert(htmlCode)
                    $menu.append(htmlCode)

            # Initialization of menu
            $('#dl-menu').dlmenu();