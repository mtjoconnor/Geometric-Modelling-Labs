/* 
 * SvLis practical
 * 
 *   Irina Voiculescu, 5 Feb 2010
 * 
 */ 
 
#include "svlis.h" 
 
#include "sv_cols.h" 
#if macintosh 
 #pragma export on 
#endif 
 
#if macintosh 
 #define RES_FILE "::mug.mod" 
 #define RAY_FILE "::raytraced.ppm" 
#else 
 #define RES_FILE "mug.mod" 
 #define RAY_FILE "raytraced.ppm" 
#endif 

static void my_reporter(sv_real progress)
{
    std::cout << "Ray tracing progress: " << progress << "%"<<std::endl;
}

sv_set buildmymodel(sv_point lowCorner, sv_point highCorner) 
{
    // Model centered in the standard [0,10]^3 practical box.
    const sv_point centre(5.0, 5.0, 5.0);

    // Mug body: finite cylindrical shell with a non-zero base thickness.
    sv_line z_axis = sv_line(sv_point(0.0, 0.0, 1.0), centre);
    sv_set outer_cyl = cylinder(z_axis, 3.0);
    sv_set inner_cyl = cylinder(z_axis, 2.2);

    sv_set body_height = cuboid(sv_point(1.6, 1.6, 2.0), sv_point(8.4, 8.4, 8.4));
    sv_set cavity_height = cuboid(sv_point(1.8, 1.8, 3.0), sv_point(8.2, 8.2, 8.4));

    sv_set mug_shell = (outer_cyl & body_height) - (inner_cyl & cavity_height);
    mug_shell = mug_shell.colour(SV_WHEAT);

    // Handle: torus segment overlapping the shell (avoid just-touching geometry).
    sv_line handle_axis = sv_line(sv_point(1.0, 0.0, 0.0), sv_point(8.1, 5.0, 5.2));
    sv_set handle_torus = torus(handle_axis, 2.1, 0.55);
    sv_set handle_clip = cuboid(sv_point(6.6, 2.1, 2.2), sv_point(10.0, 7.9, 8.0));
    sv_set handle = (handle_torus & handle_clip).colour(SV_BROWN);

    sv_set result = (mug_shell | handle).colour(SV_WHEAT);
    return result;
}


int main(int argc, char **argv)
{ 
 
    // Initialise svLis 
    glutInit(&argc, argv);//Needed under some configurations of SvLis, but not others
    svlis_init(); 
 
    // Define the corners of a box, then the box 
    sv_point b_lo = SV_OO; 
    sv_point b_hi = sv_point(10,10,10); 
    sv_box mod_box = sv_box(b_lo,b_hi); 
 
    // Construct a model in the region of interest defined by (b_lo, b_hi)
    sv_set mymodel = buildmymodel(b_lo,b_hi);
 
    // Make sure anything that needs to can find attributes 
    // (i.e. colours) where it expects. 
    mymodel = mymodel.percolate(); 
 
    // Build a model with the result and the box 
    sv_model m = sv_model(mymodel,mod_box,sv_model()); 
 
 
    // Always write the file 
    std::ofstream opf(RES_FILE); 
    if(!opf) 
    { 
	svlis_error(argv[0],"can't open output file mug.mod in directory results", SV_WARNING); 
	return(svlis_end(1)); 
    } 
    else 
    {
        // Switch on pretty_print output 
        pretty_print(1); 
	opf << m; 
    }

    bool plot=true;
    bool raytrace = false;

    if (plot)    {
      // Display on the graphics screen
      m = m.facet();
      plot_m_p_gons(m);
      // Keep the picture up until user says
      std::cout << SV_EL << SV_EL << "SvLis program "<<argv[0]<<" has finished successfully." << SV_EL << SV_EL;
    }

    else  if (raytrace) {
        // Raytrace to file "raytraced.ppm" 
        sv_integer width=200;  //pixels. 800 is good 
        sv_integer height=150; //pixels. 600 is good
        // Rendering parameters set here
        sv_view v;
	v.eye_point(sv_point(23,26,24));
	v.centre(sv_point(0,0,0));
   
	// For the raytracer we need to construct a linked list of light sources
	sv_light_list lightlist;
	sv_light_list lightlist2;

	sv_lightsource light1;
	light1.direction(sv_point(0.1,0.3,-1));
	light1.type(POINT_SOURCE);
	light1.location(sv_point(5, 2, 15));
	light1.angle_power(sv_real(2.0));
	sv_lightsource light2;
	light2.type(POINT_SOURCE);
	light2.location(sv_point(15, 5, 5));
	light2.direction(sv_point(-1,0,0));
	light2.angle_power(sv_real(2.2));

	lightlist2.source = &light2;
	lightlist2.name = "my point light";
	lightlist2.next = NULL;

	lightlist.source = &light1;
	lightlist.name = "my parallel light";
	lightlist.next = &lightlist2;

	sv_picture mypict;
	mypict.resolution(width, height);
	set_ground_colour(1.0, 1.0, 1.0, sqrt(3));//This is white background.  The sqrt(3) is there because the (r=1,b=1,g=1) vector gets normalised
    sv_real progress_interval = 5;
	sv_integer result;
	result = generate_picture(m, v, lightlist, mypict, progress_interval, my_reporter);

        write_image(RAY_FILE, &mypict, "bleh");
    }
    else {
      // NEITHER PLOT NOR RAYTRACE 
      std::cout << SV_EL << SV_EL << "Nothing to display. Set plot or raytrace variable" << SV_EL << SV_EL;
    }
    return(svlis_end(0));
}

 
#if macintosh 
 #pragma export off 
#endif 
