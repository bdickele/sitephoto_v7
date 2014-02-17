jQuery ($) ->
    # ##############################################
    # LIST OF CATEGORIES
    # ##############################################
    $categoryMenu = $('#categoryMenu')
    categoryList = $categoryMenu.data('list')
    $.get categoryList, (categories) ->
        $.each categories, (index, category) ->
            row = "<li><a href='" + jsRoutes.controllers.category.Categories.view(category.id)['url'] + "'>" + category.title + "</a></li>"
            #alert(row)
            $categoryMenu.append(row)