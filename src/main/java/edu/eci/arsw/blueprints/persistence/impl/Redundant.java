package edu.eci.arsw.blueprints.persistence.impl;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.Filter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//@Service
public class Redundant implements Filter {
    @Override
    public Blueprint filtro(Blueprint bp) {
        List<Point> points = bp.getPoints();
        List<Point> pointss = new ArrayList<>();
        for(int i = 0; i < points.size(); i++){
            if(i == points.size() - 1){
                pointss.add(points.get(i));
            }
            else if(points.get(i).getX() != points.get(i+1).getX() || points.get(i).getY() != points.get(i+1).getY()){
                pointss.add(points.get(i));
            }
        }
        bp.setPoints(pointss);
        return bp;
    }
}
