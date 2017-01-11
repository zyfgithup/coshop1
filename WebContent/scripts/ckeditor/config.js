/**
 * @license Copyright (c) 2003-2013, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.html or http://ckeditor.com/license
 */

CKEDITOR.editorConfig = function (config) {
    // Define changes to default configuration here.
    // For the complete reference:
    // http://docs.ckeditor.com/#!/api/CKEDITOR.config
    config.uiColor = '#f5f5f5';
    config.resize_enabled = false;//禁止resize
    config.width = 650; //宽度
    config.height = 180; //高度

    //从Word粘贴到文档去除格式问题
    //config.forcePasteAsPlainText =false;
    //config.pasteFromWordPromptCleanup = true;
    config.pasteFromWordKeepsStructure = false;
    config.pasteFromWordRemoveStyle = false;
    config.pasteFromWordRemoveFontStyles = false;

    config.font_names = '宋体/宋体;黑体/黑体;仿宋/仿宋_GB2312;楷体/楷体_GB2312;隶书/隶书;幼圆/幼圆;微软雅黑/微软雅黑;' + config.font_names;
    config.smiley_descriptions = [
        'smiley', 'sad', 'wink', 'laugh', 'frown', 'cheeky', 'blush', 'surprise',
        'indecision', 'angry', 'angel', 'cool', 'devil', 'crying', 'enlightened', 'no',
        'yes', 'heart', 'broken heart', 'kiss', 'mail'
    ];

    // The toolbar groups arrangement, optimized for two toolbar rows.
    config.toolbar = [
        ['Image','Source', 'Bold', 'Italic'],
        ['JustifyLeft', 'JustifyCenter', 'JustifyRight', 'JustifyBlock'],
        // ['Smiley'],
        ['Styles', 'Font', 'FontSize'],
        ['TextColor'],
        ['Undo', 'Redo']
    ];
    // Remove some buttons, provided by the standard plugins, which we don't
    // need to have in the Standard(s) toolbar.
    config.removeButtons = 'Underline,Subscript,Superscript';

    // Se the most common block elements.
    config.format_tags = 'p;h1;h2;h3;pre';

    // Make dialogs simpler.
    config.removeDialogTabs = 'image:advanced;link:advanced';
};
