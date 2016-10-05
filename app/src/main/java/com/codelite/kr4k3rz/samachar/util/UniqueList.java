package com.codelite.kr4k3rz.samachar.util;


import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.RandomAccess;
import java.util.Set;


class UniqueList<T> extends ArrayList<T> implements List<T> , Set<T> , RandomAccess
{
    private final HashMap<T,Integer> hash; // T -> int

    public UniqueList()
    {
        hash = new HashMap<>();
    }

    /*
    * O(n)
    * */
    @Override
    public void add(int location, T object)
    {
        super.add(location, object);
        for( int i = location ; i < size() ; i++ )
        {
            hash.put(get(i),i);
        }
    }

    /*
    * O(1) amortized.
    * */
    @Override
    public boolean add(T object) {
        if( hash.containsKey(object) ) return false;

        hash.put(object, size());
        super.add(object);

        return true;
    }

    /*
    * O(MAX(collection.size(),n)) because of the hash-value-shift afterwards.
    * */
    @Override
    public boolean addAll(int location, @NonNull Collection<? extends T> collection) {
        boolean bChanged = false;
        for( T t : collection)
        {
            if( ! hash.containsKey( t ) )
            {
                hash.put(t, size());
                super.add(t);
                bChanged = true;
            }
        }

        for( int i = location + collection.size() ; i < size() ; i ++ )
        {
            hash.put( get(i) , i );
        }

        return bChanged;
    }

    /*
    * O(collection.size())
    * */
    @Override
    public boolean addAll(@NonNull Collection<? extends T> collection) {
        boolean bChanged = false;
        for( T t : collection)
        {
            if( ! hash.containsKey( t ) )
            {
                hash.put( t , size() );
                super.add(t);
                bChanged = true;
            }
        }
        return bChanged;
    }

    /*
    * O(n)
    * */
    @Override
    public void clear() {
        hash.clear();
        super.clear();
    }

    /*
    * O(1)
    * */
    @Override
    public boolean contains(Object object) {
        return hash.containsKey(object);
    }

    /*
    * O(collection.size())
    * */
    @Override
    public boolean containsAll(@NonNull Collection<?> collection) {
        boolean bContainsAll = true;
        for( Object c : collection ) bContainsAll &= hash.containsKey(c);
        return bContainsAll;
    }

    /*
    * O(1)
    * */
    @Override
    public int indexOf(Object object) {
        //noinspection SuspiciousMethodCalls
        Integer index = hash.get(object);
        return index!=null?index:-1;
    }

    /*
    * O(1)
    * */
    @Override
    public int lastIndexOf(Object object)
    {
        return hash.get(object);
    }

    /*
    * O(n) because of the ArrayList.remove and hash adjustment
    * */
    @Override
    public T remove(int location) {
        T t = super.remove(location);
        hash.remove( t );
        for( int i = size() - 1 ; i >= location  ; i -- )
        {
            hash.put( get(i) , i );
        }
        return t;
    }

    /*
    * O(n) because of the ArrayList.remove and hash adjustment
    * */
    @Override
    public boolean remove(Object object) {
        Integer i = hash.get( object );
        if( i == null ) return false;
        remove( i.intValue() );
        return true;
    }

    /*
    * O( MAX( collection.size() , ArrayList.removeAll(collection) ) )
    * */
    @Override
    public boolean removeAll(@NonNull Collection<?> collection) {
        for( Object c : collection )
        {
            hash.remove( c );
        }
        return super.removeAll( collection );
    }
}