import java.awt.Rectangle;

public class Collision {

    public static void checkSomeCollision(GameObject entity, Handler handler) {

        if(!(entity instanceof Player || entity instanceof Zombie)) return;
        
        for (int i = 0; i < handler.entities.size(); i++) {
            GameObject anyObject = handler.entities.get(i);
        
            if (anyObject.getType() == EntityType.Block || anyObject.getType() == EntityType.KirilmazBlock) {
                Rectangle blockBounds = anyObject.getBounds();
                Rectangle entityBounds = entity.getBounds();
        
                boolean yatayCollision = false;
        
                Rectangle nextYBounds = new Rectangle(
                    entityBounds.x,
                    entityBounds.y + (int)entity.getSpeed_Y(),
                    entityBounds.width,
                    entityBounds.height
                );

                Rectangle nextXBounds = new Rectangle(
                        entityBounds.x + (int)entity.getSpeed_X(),
                        entityBounds.y,
                        entityBounds.width,
                        entityBounds.height
                    );
        
                if (nextYBounds.intersects(blockBounds)) {
                    yatayCollision = true; //çarpışmaların karışmaması için
        
                    if (entity.getSpeed_Y() > 0) {
                        entity.setY(blockBounds.y - entityBounds.height - 5);
                    } else if (entity.getSpeed_Y() < 0) {
                        entity.setY(blockBounds.y + blockBounds.height + 5);
                    }
                    entity.setSpeed_Y(0); 
                }
        
                if (!yatayCollision) { 
                    if (nextXBounds.intersects(blockBounds)) {
        
                        if (entity.getSpeed_X() > 0) {
                            entity.setX(blockBounds.x - entityBounds.width - 5);
                        } else if (entity.getSpeed_X() < 0) { 
                            entity.setX(blockBounds.x + blockBounds.width + 5);
                        }
                        entity.setSpeed_X(0); 
                    }
                }
            }
        
            if (anyObject.getType() == EntityType.Bullet && entity instanceof Zombie) {
                Zombie tempZombie = (Zombie)entity;
                if(entity.getBounds().intersects(anyObject.getBounds())) {
                    
                    //keskin nişancı için mermi yok edilmez,
                    //merminin değdiği zombiler büyük hasar alır hepsi direkt ölür
                    //mermi aynı güçte ilerler varsayımı
                    if(Player.gun.type != GunType.KeskinNisanci) {
                        handler.removeEntity(anyObject);
                        Player.setScore(Player.getScore() + 5);
                    }

                    else{
                        ((Zombie)entity).setHealth(((Zombie)entity).getHealth() - 5);
                        Player.setScore(Player.getScore() + 5);

                    }
                        


                    if(Player.gun.type == GunType.Roketatar) {
                        tempZombie.setHealth(tempZombie.getHealth() -3);
                        Player.setScore(Player.getScore() + 3);
                        //mermi kimin kafasına geldiyse ona 3 hasar

                        //patlamanın etki edeceği alan
                        Rectangle etkiAlani = new Rectangle(
                            anyObject.getX()-100,
                            anyObject.getY()-100,
                            200,
                            200
                        );

                        for(int j=0; j<handler.entities.size(); j++) {
                            GameObject temp = handler.entities.get(j);
                            if(temp instanceof Zombie && temp.getBounds().intersects(etkiAlani)) {
                                ((Zombie)temp).setHealth(((Zombie)temp).getHealth() - 2);
                                //alandaki her zombiye ikişer hasar
                                Player.setScore(Player.getScore() + 2);
                            }

                            else if(temp.getType() == EntityType.Block && temp.getBounds().intersects(etkiAlani)) {
                                handler.removeEntity(temp);
                            }
                        }

                        handler.addEntity(new BombEffect(anyObject.getX()-100, anyObject.getY()-100, EntityType.RoketatarEfekt, 200, 200, 60, handler));
                    }

                    else {
                        tempZombie.setHealth(tempZombie.getHealth() - 1);
                        Player.setScore(Player.getScore() + 1);
                    }
                }
            }

            if (anyObject.getType() == EntityType.Asit && entity instanceof Player) {
                if(entity.getBounds().intersects(anyObject.getBounds())) {
                    handler.removeEntity(anyObject);
                    Player.setHealth(Player.getHealth() - 8);
                }
            }

            if (entity instanceof Player && anyObject instanceof Zombie) {
                if(entity.getBounds().intersects(anyObject.getBounds())) {
                    Player.setHealth(Player.getHealth() - 0.01 * ((Zombie)anyObject).getAttack());
                }
            }
        
            if (anyObject.getType() == EntityType.YedekSarjor && entity instanceof Player) {
                if(entity.getBounds().intersects(anyObject.getBounds())) {
                    Player.gun.yedekSarjor++;
                    handler.removeEntity(anyObject);
                }
                
            }
        }
        
        
    }
}
