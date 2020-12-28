#ifdef GL_ES
precision mediump float;
#endif

uniform vec2 u_resolution;
uniform float u_time;

float a(float a){

float v;if(u_time > 50.0){
v = v +a;
}else{
v = 0.2 +step(gl_FragCoord.x, 0.4);
}for(int i = 0;i < 50;i = i + 1){
v = 0.3;
}return v;

}

void main(){

gl_FragColor = vec4(1, 1, 1, 1);

}

