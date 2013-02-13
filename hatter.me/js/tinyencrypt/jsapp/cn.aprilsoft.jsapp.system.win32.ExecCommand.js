
<SCRIPT LANGUAGE="JScript">
/*
 *该function执行copy指令
 */
function fn_doufucopy() {
 edit.select();
 document.execCommand('Copy');
}
/*
 *该function执行paste指令
 */
function fn_doufupaste() {
 tt.focus();
 document.execCommand('paste');
}
__>
</SCRIPT> 

<script language=javascript>
 /*
  *该function用来创建一个超链接
  */
  function fn_creatlink()
  {
   document.execCommand('CreateLink',true,'true');// 弹出一个对话框输入URL
   //document.execCommand('CreateLink',false,'http://www.51js.com');
  }
 /*
  *该function用来将选中的区块设为指定的背景色
  */
  function fn_change_backcolor()
  {
   document.execCommand('BackColor',true,'#FFbbDD');//true或false都可以
  }
 /*
  * 该function用来将选中的区块设为指定的前景色,改变选中区块的字体大小,改变字体,字体变粗变斜
  */
 function fn_change_forecolor()
 {
  //指定前景色
  document.execCommand('ForeColor',false,'#BBDDCC');//true或false都可以
  //指定背景色
  document.execCommand ('FontSize',false,7);    //true或false都可以
  //字体必须是系统支持的字体
  document.execCommand('FontName',false,'标楷体');   //true 或false都可以
  //字体变粗
  document.execCommand('Bold');
  //变斜体
  document.execCommand('Italic');
 }
 /*
  *该function用来将选中的区块加上不同的线条
  */
  function fn_change_selection()
  {
  //将选中的文字加下划线
  document.execCommand('Underline');
  //在选中的文字上划粗线
  document.execCommand('StrikeThrough');
  //将选中的部分文字变细
  document.execCommand('SuperScript');
  //将选中区块的下划线取消掉
  document.execCommand('Underline');
  }
  /*
   *该function用来将选中的区块排成不同的格式
   */
  function fn_format()
  {
  //有序列排列
  document.execCommand('InsertOrderedList');
  //实心无序列排列
  document.execCommand('InsertUnorderedList');
  //空心无序列排列
  document.execCommand('Indent');
  }
 /*
  *该function用来将选中的区块剪下或是删除掉
  */
 function fn_CutOrDel()
 {
  //删除选中的区块
  //document.execCommand('Delete');
  // 剪下选中的区块
  document.execCommand('Cut');
 }
 /*
  *该function用来将选中的区块重设为一个相应的物件
  */
 function fn_InsObj()
 {
  /*
   ******************************************
   * 以下指令都是为选中的区块重设一个object;
   *  如没有特殊说明,第二个参数true或false是一样的;
   *  参数三表示为该 object的id;
   *  可以用在javascript中通过其指定的id来控制它
   ******************************************
  */
  /*重设为一个button(InsertButton和InsertInputButtong一样,
  只不前者是button,后者是input)*/
  /*document.execCommand('InsertButton',false,"aa"); //true或false无效
  document.all.aa.value="风舞九天";*/
  //重设为一个 fieldset
  /*document.execCommand('InsertFieldSet',true,"aa");
  document.all.aa.innerText="刀剑如梦";*/
  //插入一个水平线
  //document.execCommand('InsertHorizontalRule',true,"aa");
  //插入一个iframe
  //document.execCommand('InsertIFrame',true,"aa");
  //插入一个InsertImage,设为true时需要图片 ,false时不需图片
  //document.execCommand('InsertImage',false,"aa");
  //插入一个checkbox
  //document.execCommand('InsertInputCheckbox',true,"aa");
  //插入一个file类型的object
  //document.execCommand('InsertInputFileUpload',false,"aa");
  //插入一个hidden
  /*document.execCommand('InsertInputHidden',false,"aa");
  alert(document.all.aa.id);*/
  //插入一个InputImage
  /*document.execCommand('InsertInputImage',false,"aa");
  document.all.aa.src=" F-a10.gif";*/
  //插入一个Password
  //document.execCommand('InsertInputPassword',true,"aa");
  //插入一个Radio
  //document.execCommand('InsertInputRadio',false,"aa");
  //插入一个Reset
  //document.execCommand('InsertInputReset',true,"aa");
  //插入一个Submit
  //document.execCommand('InsertInputSubmit',false,"aa");
  //插入一个input text
  //document.execCommand('InsertInputText',false,"aa");
  //插入一个textarea
  //document.execCommand('InsertTextArea',true,"aa");
  //插入一个 select list box
  //document.execCommand('InsertSelectListbox',false,"aa");
  //插入一个single select
  document.execCommand('InsertSelectDropdown',true,"aa");
  //插入一个line break(硬回车??)
  //document.execCommand('InsertParagraph');
  //插入一个marquee
  /*document.execCommand('InsertMarquee',true,"aa");
  document.all.aa.innerText="bbbbb";*/
  //用于取消选中的阴影部分
  //document.execCommand('Unselect');
  //选中页面上的所有元素
  //document.execCommand('SelectAll');
 }
 /*
  *该function用来将页面保存为一个文件
  */
 function fn_save()
 {
  //第二个参数为欲保存的文件名
  document.execCommand('SaveAs','mycodes.txt ');
  //打印整个页面
  //document.execCommand('print');
 }
</script>