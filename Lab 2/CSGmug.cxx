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
    // Center mug in the practical box.
    const sv_point centre = (lowCorner + highCorner)/2.0;
    const sv_line z_axis = sv_line(sv_point(0.0, 0.0, 1.0), centre);

    // Main body + cavity (straight mug profile).
    sv_set outer_body = cylinder(z_axis, 2.60) & cuboid(sv_point(2.2, 2.2, 1.9), sv_point(7.8, 7.8, 8.2));
    sv_set inner_body = cylinder(z_axis, 2.15) & cuboid(sv_point(2.4, 2.4, 2.6), sv_point(7.6, 7.6, 8.05));
    sv_set shell = outer_body - inner_body;

    // White ceramic lip near the top.
    sv_set lip_outer = cylinder(z_axis, 2.72) & cuboid(sv_point(2.1, 2.1, 7.85), sv_point(7.9, 7.9, 8.32));
    sv_set lip_inner = cylinder(z_axis, 2.22) & cuboid(sv_point(2.3, 2.3, 7.85), sv_point(7.7, 7.7, 8.32));
    sv_set lip = lip_outer - lip_inner;

    // Thin decorative band below the lip.
    sv_set band_outer = cylinder(z_axis, 2.68) & cuboid(sv_point(2.1, 2.1, 7.15), sv_point(7.9, 7.9, 7.34));
    sv_set band_inner = cylinder(z_axis, 2.48) & cuboid(sv_point(2.25, 2.25, 7.15), sv_point(7.75, 7.75, 7.34));
    sv_set band = band_outer - band_inner;

    // Side handle in the correct orientation (vertical loop on mug side).
    sv_line handle_axis = sv_line(sv_point(0.0, 1.0, 0.0), sv_point(8.10, 5.0, 5.05));
    sv_set handle_loop = torus(handle_axis, 1.55, 0.36);
    sv_set handle_clip = cuboid(sv_point(7.0, 4.45, 2.95), sv_point(10.2, 5.55, 7.15));
    sv_set handle = handle_loop & handle_clip;

    // Blend handle joints into the body.
    sv_set joint_top = sphere(sv_point(7.48, 5.0, 6.45), 0.33);
    sv_set joint_bot = sphere(sv_point(7.48, 5.0, 3.65), 0.33);
    handle = handle | joint_top | joint_bot;

    // Apply reference-like colors.
    sv_set red_body = shell.colour(SV_RED);
    sv_set white_lip = lip.colour(SV_WHITE);
    sv_set gold_band = band.colour(SV_GOLD);
    sv_set red_handle = handle.colour(SV_FIREBRICK);

    return red_body | white_lip | gold_band | red_handle;
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
      set_user_grad_fac(0.20); // Smaller value => smoother curved surfaces.
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
