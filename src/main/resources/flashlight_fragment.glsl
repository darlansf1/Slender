#version 150

struct LightProperties
{
	vec4 position;
	vec4 ambientColor;
	vec4 diffuseColor;
	vec4 specularColor;
	float constantAttenuation;
    float linearAttenuation;
    float quadraticAttenuation;
};

struct MaterialProperties
{
	vec4 ambientColor;
	vec4 diffuseColor;
	vec4 specularColor;
	float specularExponent;
};

uniform	LightProperties u_light;
uniform	MaterialProperties u_material;

uniform sampler2D u_texture;
uniform bool u_is_texture;

in vec4 v_position;
in vec3 v_normal;
in vec3 v_eye;
in vec2 v_texcoord;

out vec4 fragColor;

void main(void){
	//luz sempre junto com a camera
	float d = length(v_eye);
	vec3 l = normalize(v_eye);
	vec3 n = normalize(v_normal);

	vec4 ambient = u_light.ambientColor * u_material.ambientColor;
	vec4 diffuse = vec4(0.0, 0.0, 0.0, 0.0);
	vec4 specular = vec4(0.0, 0.0, 0.0, 0.0);

	float lDotN = max(dot(l, n), 0.0);
	if (lDotN > 0.0){
		float intensity = max(dot(n, l), 0.0);
		if(u_is_texture)
	        diffuse = intensity * texture(u_texture, v_texcoord) * lDotN;
	    else
	        diffuse = intensity * u_light.diffuseColor * u_material.diffuseColor * lDotN;
		    
		    
		if (intensity > 0.0){
			vec3 r = reflect(-l, n);
			float specExponent = pow(max(dot(r, n), 0.0), u_material.specularExponent);
			specular = u_light.specularColor * u_material.specularColor * specExponent;
		}
	}

	//spotlight calculation
	float magicalangle = dot(l, vec3(0.0, 0.0, 1.0));
	float att = 1.0 / (u_light.constantAttenuation + 
	            (u_light.linearAttenuation*d) + 
	            (u_light.quadraticAttenuation*d*d));
		
	if(magicalangle > 0.9838 && magicalangle < 0.984){
		att = att*0.35;
	}else if (magicalangle < 0.98){
		att = att*0.5;
	}

	fragColor = max(att*(diffuse + specular), ambient);
}