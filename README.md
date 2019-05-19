# Selector

it combine different choice mode into one View. There are two default mode, one is single choice mode act like `RadioButton + RadioGroup`, the other is multiple choice mode act like `CheckBox`. The Choice mode could be extended dynamically.

### why this?
1. the RadioButton + RadioGroup has limitation on it's items, they have to be honrizontal or vertical. Use this to get rid of it. You could arrange items whatever you like in layout xml
2. it combine different choice mode into one View. Single choice, multiple choice and you could design the mode you like. May be it is an "order mode", which is a combination of single and multiple choice.(just like the case when ordering dinner)
3. you could add Animation for selected and unselected state.
4. you could design what radio button looks like by a layout xml.

### how does it looks it
let me show you the "order mode":
![order-choice.gif](https://user-gold-cdn.xitu.io/2019/5/19/16ad08e61cd8205b?w=960&h=640&f=gif&s=2127202)

<iframe height=500 width=500 src="https://user-gold-cdn.xitu.io/2019/5/19/16ad08e61cd8205b?w=960&h=640&f=gif&s=2127202">
